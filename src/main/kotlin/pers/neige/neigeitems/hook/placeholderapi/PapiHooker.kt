package pers.neige.neigeitems.hook.placeholderapi

import javassist.ClassClassPath
import javassist.ClassPool
import org.bukkit.OfflinePlayer
import org.slf4j.LoggerFactory
import pers.neige.neigeitems.JvmHacker
import java.lang.instrument.ClassDefinition
import java.util.function.BiFunction

/**
 * PlaceholderAPI挂钩
 */
abstract class PapiHooker {
    private companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(PapiHooker::class.java.simpleName)
    }

    fun hack() {
        try {
            val pool = ClassPool.getDefault()
            pool.insertClassPath(ClassClassPath(Class.forName("me.clip.placeholderapi.PlaceholderAPIPlugin")))
            pool.insertClassPath(ClassClassPath(Class.forName("org.bukkit.command.CommandSender")))
            pool.insertClassPath(ClassClassPath(Class.forName("pers.neige.neigeitems.event.PapiReloadEvent")))
            val ctClass = pool.get("me.clip.placeholderapi.PlaceholderAPIPlugin")
            ctClass.defrost()

            val reloadConf =
                ctClass.getDeclaredMethod("reloadConf", arrayOf(pool.get("org.bukkit.command.CommandSender")))
            reloadConf.insertAfter("new pers.neige.neigeitems.event.PapiReloadEvent().call();")
            JvmHacker.instrumentation().redefineClasses(
                ClassDefinition(
                    Class.forName("me.clip.placeholderapi.PlaceholderAPIPlugin"), ctClass.toBytecode()
                )
            )
        } catch (exception: IllegalStateException) {
            if (exception.message == "Failed to attach agent hacker") {
                logger.info("你他妈了个逼的不会是用jre开的服吧？")
            }
            exception.printStackTrace()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    /**
     * 解析一段文本中的papi变量, 不解析其中的颜色代码
     * 在以往的众多版本中, papi都会强制解析文本中的代码
     * 在2.11.2版本中, papi移除了该功能
     * 不过, 管他呢, 自己实现就完事儿了
     *
     * @param player 用于解析PAPI变量的玩家对象
     * @param text 待解析文本
     * @return 解析后文本
     */
    abstract fun papi(player: OfflinePlayer?, text: String): String

    /**
     * 将文本中的所有papi变量改写为papi节点
     *
     * @param text 待转换文本
     * @return 转换后文本
     */
    abstract fun toSection(text: String): String

    /**
     * 判断文本中是否存在有效papi变量
     *
     * @param text 待检测文本
     * @return 是否存在有效papi变量
     */
    abstract fun hasPapi(text: String): Boolean

    /**
     * 例：%player_name%
     * identifier -> player
     * parameters -> name
     *
     * @param player 用于解析PAPI变量的玩家对象
     * @param identifier PAPI标识符
     * @param parameters PAPI参数
     * @return 解析后文本
     */
    abstract fun request(player: OfflinePlayer, identifier: String, parameters: String): String

    /**
     * 卸载papi扩展
     *
     * @param expansion papi扩展
     */
    abstract fun unregisterExpansion(expansion: PlaceholderExpansion)

    /**
     * 新建一个papi扩展
     *
     * @param identifier papi扩展名
     * @param author 扩展作者
     * @param version 扩展版本
     * @param executor 变量处理器
     * @return papi扩展
     */
    fun newPlaceholderExpansion(
        identifier: String, author: String, version: String, executor: BiFunction<OfflinePlayer?, String, String>
    ): PlaceholderExpansion {
        return PlaceholderExpansion(identifier, author, version, executor)
    }
}