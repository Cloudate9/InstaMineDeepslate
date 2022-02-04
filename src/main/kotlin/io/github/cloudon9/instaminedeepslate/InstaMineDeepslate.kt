package io.github.cloudon9.instaminedeepslate

import io.github.cloudon9.instaminedeepslate.command.IMDCommand
import io.github.cloudon9.instaminedeepslate.listener.MiningDeepslate
import io.github.cloudon9.instaminedeepslate.listener.UpdateInformer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bstats.bukkit.Metrics
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.*


class InstaMineDeepslate : JavaPlugin() {

    var updateFound = false; //Publically exposed

    override fun onEnable() {

        config.options().copyDefaults(true).parseComments(true)
        saveConfig()

        val pluginManager = Bukkit.getPluginManager()
        pluginManager.registerEvents(MiningDeepslate(config), this)
        pluginManager.registerEvents(UpdateInformer(config, this), this)
        getCommand("instaminedeepslate")!!.setExecutor(IMDCommand(config, this))

        Metrics(this, 13733)
        updateCheck(this, true)

    }

    private fun updateCheck(plugin: JavaPlugin, firstCheck: Boolean) {
        object : BukkitRunnable() {
            override fun run() {
                try {
                    val readGit = Scanner(
                        InputStreamReader(
                            URL(
                                "https://raw.githubusercontent.com/CloudOn9/InstaMineDeepslate/master/build.gradle.kts"
                            ).openStream()
                        )
                    )

                    var version = ""
                    while (readGit.hasNext()) {
                        val line = readGit.nextLine()

                        if (line.startsWith("version = ")) {
                            //Targeted line example: version = "2.0.0"
                            version = line.split('"')[1].removeSuffix("\"")
                            break
                        }
                    }

                    if (version == description.version) {
                        if (firstCheck)
                            logger.info(
                                Component.text(
                                    "is up to date!", TextColor.color(5636095) //Aqua
                                ).content()
                            )

                        server.scheduler.scheduleSyncDelayedTask(
                            plugin, { updateCheck(plugin, false) }, 576000
                        )
                        return
                    }

                    logger.info(
                        Component.text(
                            "can be updated at " +
                                    "https://www.curseforge.com/minecraft/bukkit-plugins/insta-mine-deepslate/\n" +
                                    "If you don't see the update yet, come back in 24-48 hours. It should be ready by then.",
                            TextColor.color(5636095) //Aqua
                        ).content()
                    )
                } catch (ex: IOException) {
                    logger.info(
                        Component.text(
                            "Failed to check for updates!", TextColor.color(16733525)
                        ).content()
                    )
                    server.scheduler.scheduleSyncDelayedTask(
                        plugin, { updateCheck(plugin, false) }, 576000
                    )
                }
            }
        }.runTaskAsynchronously(this)
    }

}
