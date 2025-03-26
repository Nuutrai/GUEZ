package com.nuutrai.guez.events

import com.nuutrai.guez.gui.GUIManager
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class InventoryClick : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onInventoryClickEvent(event: InventoryClickEvent) {

        val gui = GUIManager.listeningGUIs[event.clickedInventory] ?: return
        if (gui.lock) event.isCancelled = true
        ((gui.slotMap[event.slot] ?: return).action ?: return).accept(event)

    }

}