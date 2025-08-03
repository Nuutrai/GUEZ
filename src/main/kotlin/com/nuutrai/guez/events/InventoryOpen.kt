package com.nuutrai.guez.events

import com.nuutrai.guez.gui.GUIManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent

class InventoryOpen : Listener {
    @EventHandler
    fun onInventoryOpen(event: InventoryOpenEvent) {
        val gui = GUIManager.listeningGUIs[event.inventory] ?: return
        gui.open?.accept(event)
    }
}