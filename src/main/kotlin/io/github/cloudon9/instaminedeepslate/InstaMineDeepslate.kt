package io.github.cloudon9.instaminedeepslate

import io.github.cloudon9.instaminedeepslate.command.IMDCommand
import io.github.cloudon9.instaminedeepslate.listener.MiningDeepslate
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class InstaMineDeepslate: JavaPlugin() {

    override fun onEnable() {

        config.options().copyDefaults(true)
        config.options().parseComments(true)
        saveConfig()

        Bukkit.getPluginManager().registerEvents(MiningDeepslate(config), this)
        getCommand("instaminedeepslate")!!.setExecutor(IMDCommand(config, this))

        Metrics(this, 13733)

    }
}
