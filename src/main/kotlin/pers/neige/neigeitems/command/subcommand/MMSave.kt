package pers.neige.neigeitems.command.subcommand

import org.bukkit.entity.Player
import pers.neige.neigeitems.command.subcommand.Help.help
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.HookerManager
import pers.neige.neigeitems.manager.HookerManager.mythicMobsHooker
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.LangUtils.sendLang
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.module.nms.getName
import java.io.File

object MMSave {
    // ni mm load [物品ID] (保存路径) > 将对应ID的MM物品保存为NI物品
    val load = subCommand {
        execute<Player> { sender, _, _ ->
            submit(async = true) {
                help(sender)
            }
        }
        // ni mm load [物品ID]
        dynamic {
            suggestion<Player>(uncheck = true) { _, _ ->
                mythicMobsHooker!!.getItemIds()
            }
            execute<Player> { sender, _, argument ->
                submit(async = true) {
                    mythicMobsHooker!!.getItemStackSync(argument)?.let { itemStack ->
                        when (ItemManager.saveItem(itemStack, argument, "$argument.yml", false)) {
                            // 保存成功
                            1 -> {
                                sender.sendLang("Messages.successSaveInfo", mapOf(
                                    Pair("{name}", itemStack.getName()),
                                    Pair("{itemID}", argument),
                                    Pair("{path}", "$argument.yml")
                                ))
                            }
                            // 已存在对应ID物品
                            0 -> {
                                sender.sendLang("Messages.existedKey", mapOf(
                                    Pair("{itemID}", argument)
                                ))
                            }
                        }
                        // 未知物品
                    } ?: let {
                        sender.sendLang("Messages.unknownItem", mapOf(
                            Pair("{itemID}", argument)
                        ))
                    }
                }
            }
            // ni mm load [物品ID] (保存路径)
            dynamic {
                suggestion<Player>(uncheck = true) { _, _ ->
                    ItemManager.files.map { it.path.replace("plugins${File.separator}NeigeItems${File.separator}Items${File.separator}", "") }
                }
                execute<Player> { sender, context, argument ->
                    submit(async = true) {
                        mythicMobsHooker!!.getItemStackSync(context.argument(-1))?.let { itemStack ->
                            when (ItemManager.saveItem(itemStack, context.argument(-1), argument, false)) {
                                // 保存成功
                                1 -> {
                                    sender.sendLang("Messages.successSaveInfo", mapOf(
                                        Pair("{name}", itemStack.getName()),
                                        Pair("{itemID}", context.argument(-1)),
                                        Pair("{path}", argument)
                                    ))
                                }
                                // 已存在对应ID物品
                                0 -> {
                                    sender.sendLang("Messages.existedKey", mapOf(
                                        Pair("{itemID}", context.argument(-1))
                                    ))
                                }
                            }
                            // 未知物品
                        } ?: let {
                            sender.sendLang("Messages.unknownItem", mapOf(
                                Pair("{itemID}", context.argument(-1))
                            ))
                        }
                    }
                }
            }
        }
    }

    // ni mm cover [物品ID] (保存路径) > 将对应ID的MM物品覆盖为NI物品
    val cover = subCommand {
        execute<Player> { sender, _, _ ->
            submit(async = true) {
                help(sender)
            }
        }
        // ni mm cover [物品ID]
        dynamic {
            suggestion<Player>(uncheck = true) { _, _ ->
                mythicMobsHooker!!.getItemIds()
            }
            execute<Player> { sender, _, argument ->
                submit(async = true) {
                    mythicMobsHooker!!.getItemStackSync(argument)?.let { itemStack ->
                        if (ItemManager.saveItem(itemStack, argument, "$argument.yml", true) != 2) {
                            // 保存成功
                            sender.sendLang("Messages.successSaveInfo", mapOf(
                                Pair("{name}", itemStack.getName()),
                                Pair("{itemID}", argument),
                                Pair("{path}", "$argument.yml")
                            ))
                        }
                        // 未知物品
                    } ?: let {
                        sender.sendLang("Messages.unknownItem", mapOf(
                            Pair("{itemID}", argument)
                        ))
                    }
                }
            }
            // ni cover [物品ID] (保存路径)
            dynamic {
                suggestion<Player>(uncheck = true) { _, _ ->
                    ItemManager.files.map { it.path.replace("plugins${File.separator}NeigeItems${File.separator}Items${File.separator}", "") }
                }
                execute<Player> { sender, context, argument ->
                    submit(async = true) {
                        mythicMobsHooker!!.getItemStackSync(context.argument(-1))?.let { itemStack ->
                            if (ItemManager.saveItem(itemStack, context.argument(-1), argument, true) != 2) {
                                // 保存成功
                                sender.sendLang("Messages.successSaveInfo", mapOf(
                                    Pair("{name}", itemStack.getName()),
                                    Pair("{itemID}", context.argument(-1)),
                                    Pair("{path}", argument),
                                ))
                            }
                            // 未知物品
                        } ?: let {
                            sender.sendLang("Messages.unknownItem", mapOf(
                                Pair("{itemID}", context.argument(-1))
                            ))
                        }
                    }
                }
            }
        }
    }

    // ni mm loadAll (保存路径) > 将全部MM物品转化为NI物品
    val loadAll = subCommand {
        // ni mm loadAll
        execute<Player> { sender, _, _ ->
            submit(async = true) {
                mythicMobsHooker!!.getItemIds().forEach { id ->
                    mythicMobsHooker!!.getItemStackSync(id)?.let { itemStack ->
                        when (ItemManager.saveItem(itemStack, id, ConfigManager.config.getString("Main.MMItemsPath") ?: "MMItems.yml", false)) {
                            // 保存成功
                            1 -> {
                                sender.sendLang("Messages.successSaveInfo", mapOf(
                                    Pair("{name}", itemStack.getName()),
                                    Pair("{itemID}", id),
                                    Pair("{path}", ConfigManager.config.getString("Main.MMItemsPath") ?: "MMItems.yml")
                                ))
                            }
                            // 已存在对应ID物品
                            0 -> {
                                sender.sendLang("Messages.existedKey", mapOf(
                                    Pair("{itemID}", id)
                                ))
                            }
                        }
                    }
                }
            }
        }
        // ni mm loadAll (保存路径)
        dynamic {
            suggestion<Player>(uncheck = true) { _, _ ->
                ItemManager.files.map { it.path.replace("plugins${File.separator}NeigeItems${File.separator}Items${File.separator}", "") }
            }
            execute<Player> { sender, _, argument ->
                submit(async = true) {
                    mythicMobsHooker!!.getItemIds().forEach { id ->
                        mythicMobsHooker!!.getItemStackSync(id)?.let { itemStack ->
                            when (ItemManager.saveItem(itemStack, id, argument, false)) {
                                // 保存成功
                                1 -> {
                                    sender.sendLang("Messages.successSaveInfo", mapOf(
                                        Pair("{name}", itemStack.getName()),
                                        Pair("{itemID}", id),
                                        Pair("{path}", argument)
                                    ))
                                }
                                // 已存在对应ID物品
                                0 -> {
                                    sender.sendLang("Messages.existedKey", mapOf(
                                        Pair("{itemID}", id)
                                    ))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}