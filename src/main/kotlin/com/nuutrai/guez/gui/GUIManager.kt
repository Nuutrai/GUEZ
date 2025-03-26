package com.nuutrai.guez.gui

import org.bukkit.inventory.Inventory

/**
 * Manages all GUIs, mainly for events
 */
object GUIManager {

    internal val listeningGUIs = mutableMapOf<Inventory, GUI>()

    /**
     * Returns a list of the current GUIs that are being listened to
     */
    fun getGUIs(): MutableMap<Inventory, GUI> {
        return listeningGUIs.toMutableMap()
    }

}