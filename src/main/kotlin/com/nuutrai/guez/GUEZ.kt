package com.nuutrai.guez

import com.nuutrai.guez.gui.GUI
import com.nuutrai.guez.gui.GUIManager
import com.nuutrai.guez.gui.Slot
import org.bukkit.inventory.Inventory

object GUEZ {

    fun buildGUI(): GUI.GUIBuilder {
        return GUI.GUIBuilder()
    }

    fun buildSlot(): Slot.SlotBuilder {
        return Slot.SlotBuilder()
    }

    fun createGUI(init: GUI.() -> Unit): Inventory {
        val gui = GUI()
        gui.init()
        gui.toBukkit()
        return gui.inventory!!
    }

    fun editGUI(inventory: Inventory, init: GUI.() -> Unit) {
        editGUI(GUIManager.listeningGUIs[inventory]!!, init)
    }

    fun editGUI(gui: GUI, init: GUI.() -> Unit) {
        val newGUI = gui
        newGUI.init()
        gui.update(newGUI)
    }


}