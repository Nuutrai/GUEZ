package com.nuutrai.guez.events

import com.nuutrai.guez.gui.GUIManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent

class InventoryClose : Listener {
    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val gui = GUIManager.listeningGUIs[event.inventory] ?: return
        if (!gui.isPersistent) GUIManager.listeningGUIs.remove(event.inventory)
    }
}