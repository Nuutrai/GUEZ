package com.nuutrai.guez.gui

import org.bukkit.inventory.Inventory

object GUIManager {

    internal val listeningGUIs = mutableMapOf<Inventory, GUI>()
    fun getGUIs(): MutableMap<Inventory, GUI> {
        return listeningGUIs.toMutableMap()
    }

}