package com.nuutrai.guez

import com.nuutrai.guez.gui.createGUI
import com.nuutrai.guez.gui.editGUI
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class GUEZ : JavaPlugin() {

    override fun onEnable() {
        val inv = createGUI {
            name = "Shop"
            size = 54
            slot(0) {
                item = ItemStack.of(Material.DIAMOND)
                action { event ->
                    val player = event.whoClicked as Player
                    player.sendMessage(Component.text("Wow! You clicked this :D"))
                }
            }
        }
        println(inv.getItem(0)?.type)
        editGUI(inv) {
            name = "Wow"
            slot(0) {
                item = ItemStack.of(Material.DIAMOND_BLOCK)
            }
        }
        println(inv.getItem(0)?.type)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
