package io.github.cloudon9.instaminedeepslate.command

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.util.StringUtil

class IMDCommand(private val config: FileConfiguration, private val plugin: Plugin)
    : CommandExecutor, TabExecutor {

    private fun sendConfigMessage(receiver: CommandSender, key: String) {
        receiver.sendMessage(
            LegacyComponentSerializer.legacyAmpersand().deserialize(config.getString(key)!!)
        )
    }

    override fun onCommand(sender: CommandSender, command: Command, alias: String, args: Array<out String>): Boolean {
        if (sender is Player && !sender.hasPermission("instaminedeepslate.changespeed")) {
            sendConfigMessage(sender, "message.noPermission")
            return true
        }

        if (args.isEmpty()) {
            sendConfigMessage(sender, "message.invalidArguments")
            return true
        }

        when (args[0].lowercase()) {
            "enable", "on", "true" -> config["pluginActive"] = true
            "disable", "off", "false" -> config["pluginActive"] = false
            else -> {
                sendConfigMessage(sender, "message.invalidArguments")
                return true
            }
        }

        config.options().parseComments(true)
        plugin.saveConfig()
        sendConfigMessage(sender, "message.success")
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
