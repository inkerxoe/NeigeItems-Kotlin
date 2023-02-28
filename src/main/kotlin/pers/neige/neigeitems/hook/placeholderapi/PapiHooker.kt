package pers.neige.neigeitems.hook.placeholderapi

import org.bukkit.OfflinePlayer
import java.util.function.BiFunction

/**
 * PlaceholderAPI挂钩
 */
abstract class PapiHooker {
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
    abstract fun papi(player: OfflinePlayer, text: String): String

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
     * 新建一个papi扩展
     *
     * @param identifier papi扩展名
     * @param author 扩展作者
     * @param version 扩展版本
     * @param executor 变量处理器
     * @return papi扩展
     */
    fun newPlaceholderExpansion(
        identifier: String,
        author: String,
        version: String,
        executor: BiFunction<OfflinePlayer, String, String>
    ): PlaceholderExpansion {
        return PlaceholderExpansion(identifier, author, version, executor)
    }
}