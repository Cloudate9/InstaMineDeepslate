package io.github.cloudon9.instaminedeepslate.listener

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random


class MiningDeepslate(private val config: FileConfiguration): Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onMine(e: PlayerInteractEvent) {
        if (e.action != Action.LEFT_CLICK_BLOCK || e.player.gameMode != GameMode.SURVIVAL) return
        if (!config.getBoolean("pluginActive")) return
        if (!e.player.hasPermission("instaminedeepslate.eligible")) return

        val block = e.clickedBlock ?: return
        val tool = e.item ?: return

        if (block.type != Material.DEEPSLATE) return
        if (tool.type != Material.NETHERITE_PICKAXE) return

        val toolMeta = tool.itemMeta as Damageable
        if (toolMeta.enchants[Enchantment.DIG_SPEED] != 5) return

        if ((e.player.getPotionEffect(PotionEffectType.FAST_DIGGING)?.amplifier ?: return) < 2) return

        e.isCancelled = true
        e.player.world.dropItemNaturally(e.interactionPoint!!, ItemStack(block.type))
        block.type = Material.AIR

        //We cancelled the event, but we still need to decrease pickaxe durability.
        val unbreakingLevel = tool.itemMeta.enchants[Enchantment.DURABILITY] ?: 0
        if (Random.nextInt(unbreakingLevel + 1) == 0) {
            toolMeta.damage -= 1
        }

    }


}
