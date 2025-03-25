package com.nuutrai.guez.gui

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.lang.Math.clamp
import java.util.function.Consumer

class GUI {
    
    var name = ""
    var size = 27
        set(value) {
            var assert = value
            assert = (assert / 9) * 9
            if (assert < 9) assert = 9
            if (assert > 54) assert = 54
            field = assert
        }
    var lock = true
    var inventory: Inventory? = null
        private set
    var slotMap = mutableMapOf<Int, Slot>()
        private set

    fun slot(slotIndex: Int, init: Slot.() -> Unit) {
        val slot = Slot()
        slot.init()
        setSlot(slotIndex, slot)
        return
    }

    internal fun setSlot(slotIndex: Int, slot: Slot) {
        slotMap[slotIndex] = slot
    }

    fun toBukkit() {
        val inventory = Bukkit.createInventory(null, size, Component.text(name))
        for (i in 0..<inventory.size) {
            val slot = slotMap[i]
            val itemFromSlot = slot?.item
            val item =  itemFromSlot ?: ItemStack.of(Material.AIR)
            inventory.setItem(i, item)
        }
        GUIManager.listeningGUIs[inventory] = this
        this.inventory = inventory
    }

    fun update(newGUI: GUI) {
        if (newGUI.name != name || newGUI.size != size) {
            GUIManager.listeningGUIs.remove(inventory)
            newGUI.toBukkit()
            inventory = newGUI.inventory!!
            return
        }
        slotMap = newGUI.slotMap
        for (i in 0..<size) {
            inventory!!.setItem(i, slotMap[i]?.item ?: ItemStack.of(Material.AIR))
        }
    }
}

class Slot {

    var item: ItemStack = ItemStack.of(Material.AIR)
    var action: Consumer<InventoryClickEvent>? = null

    fun action(consumer: Consumer<InventoryClickEvent>) {
        action = consumer
    }

}

fun buildGUI(): GUIBuilder {
    return GUIBuilder()
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
