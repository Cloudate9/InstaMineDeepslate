package io.github.cloudate9.instaminedeepslate.listener

import io.github.cloudate9.instaminedeepslate.InstaMineDeepslate
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class UpdateInformer(
    private val config: FileConfiguration,
    private val miniMessage: MiniMessage,
    private val plugin: InstaMineDeepslate
) : Listener {

    @EventHandler
    fun informUpdate(e: PlayerJoinEvent) {
        if (!e.player.hasPermission("instaminedeepslate.updater")) return
        if (config.getBoolean("updateCheck") && plugin.updateFound) {
            e.player.sendMessage(
                miniMessage
                    .deserialize(config.getString("message.miniMessage.updateFound")!!)
                    .clickEvent(
                        ClickEvent.openUrl(
                            "https://www.curseforge.com/minecraft/bukkit-plugins/insta-mine-deepslate/"
                        )
                    )
            )
        }
    }
}