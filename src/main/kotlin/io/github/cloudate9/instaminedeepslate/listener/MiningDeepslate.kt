package io.github.cloudate9.instaminedeepslate.listener

import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
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


class MiningDeepslate(private val config: FileConfiguration) : Listener {

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

        //Amplifier 1 means Haste II
        if ((e.player.getPotionEffect(PotionEffectType.FAST_DIGGING)?.amplifier ?: return) < 1) return

        val hasSilkTouch = toolMeta.hasEnchant(Enchantment.SILK_TOUCH)

        e.isCancelled = true
        e.player.world.dropItemNaturally(
            block.location,
            ItemStack(
                if (hasSilkTouch) block.type else Material.COBBLED_DEEPSLATE
            )
        )

        val sound = if (block.type.name.contains("polished"))
            Sound.sound(
                Key.key("block.polished_deepslate.break"), Sound.Source.BLOCK, 1f, 1f
            )
        else if (block.type.name.contains("tiles"))
            Sound.sound(
                Key.key("block.deepslate_tiles.break"), Sound.Source.BLOCK, 1f, 1f
            )
        else if (block.type.name.contains("bricks"))
            Sound.sound(
                Key.key("block.deepslate_bricks.break"), Sound.Source.BLOCK, 1f, 1f
            )
        else Sound.sound(
            Key.key("block.deepslate.break"), Sound.Source.BLOCK, 1f, 1f
        )

        block.type = Material.AIR

        e.player.playSound(sound, block.location.x, block.location.y, block.location.z)

        //We cancelled the event, but we still need to decrease pickaxe durability.
        val unbreakingLevel = toolMeta.enchants[Enchantment.DURABILITY] ?: 0
        if (Random.nextInt(unbreakingLevel + 1) == 0) {
            toolMeta.damage += 1
        }
        tool.itemMeta = toolMeta

    }


}
