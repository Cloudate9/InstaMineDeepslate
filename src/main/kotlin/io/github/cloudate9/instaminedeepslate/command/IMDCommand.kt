package io.github.cloudate9.instaminedeepslate.command

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.util.StringUtil

class IMDCommand(
    private val config: FileConfiguration,
    private val miniMessage: MiniMessage,
    private val plugin: Plugin
) : CommandExecutor, TabExecutor {


    override fun onCommand(sender: CommandSender, command: Command, alias: String, args: Array<out String>): Boolean {
        if (sender is Player && !sender.hasPermission("instaminedeepslate.changespeed")) {
            sender.sendMessage(
                miniMessage.parse(
                    config.getString("message.miniMessage.noPermission")!!
                )
            )
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage(
                miniMessage.parse(
                    config.getString("message.miniMessage.invalidArguments")!!
                )
            )
            return true
        }

        when (args[0].lowercase()) {
            "enable", "on", "true" -> config["pluginActive"] = true
            "disable", "off", "false" -> config["pluginActive"] = false
            else -> {
                sender.sendMessage(
                    miniMessage.parse(
                        config.getString("message.miniMessage.invalidArguments")!!
                    )
                )
                return true
            }
        }

        config.options().copyHeader(true)
        plugin.saveConfig()
        sender.sendMessage(
            miniMessage.parse(
                config.getString("message.miniMessage.success")!!
            )
        )
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>? {

        if (sender is Player && !sender.hasPermission("instaminedeepslate.changespeed")) return null
        if (args.size == 1)
            return StringUtil.copyPartialMatches(args[0], listOf("enable", "disable"), ArrayList())

        return null
    }
}
