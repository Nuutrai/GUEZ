package com.nuutrai.guez.gui

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.function.Consumer
import kotlin.math.round

/**
 * The GUI class
 *
 * To ensure that the size is set correctly, the size is set to the closest multiple of 9, and is clamped from 9 to 54
 *
 * When a GUI is not persistent (defaults to not persistent), the GUI object is deleted when the player closes the inventory.
 * If you would like the GUI to stay in memory (E.g. if you're reüsing the same inventory object in your code) set this to true.
 *
 * @param name The name of the inventory shown to the player
 * @param size The size of the inventory
 * @param lock The locked (ability for players to move items) status (defaults to true)
 * @param isPersistent Determines whether the inventory object will be deleted (defaults to false)
 * @property slotMap A map of slot indices to [Slot] objects, this will be used to populate the inventory
 */
class GUI() {

    private constructor(builder: GUIBuilder) : this() {
        name = builder.name
        size = builder.size
        lock = builder.lock
        slotMap = builder.slotMap
    }

    var name = ""
    var size = 27
        set(value) {
            var assert = value
            assert = (round(assert.toDouble() / 9) * 9).toInt()
            if (assert < 9) assert = 9
            if (assert > 54) assert = 54
            field = assert
        }
    var lock = true
    var isPersistent = false
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

    fun slot(slotIndices: List<Int>, slot: Slot.() -> Unit) {
        slotIndices.forEach { i ->
            slot(i, slot)
        }
    }

    fun slot(slotIndices: IntRange, slot: Slot.() -> Unit) {
        slot(slotIndices.toList(), slot)
    }

    private fun setSlot(slotIndex: Int, slot: Slot) {
        slotMap[slotIndex] = slot
    }

    fun toBukkit(): Inventory {
        val inventory = Bukkit.createInventory(null, size, Component.text(name))
        for (i in 0..<inventory.size) {
            val slot = slotMap[i]
            val itemFromSlot = slot?.item
            val item =  itemFromSlot ?: ItemStack.of(Material.AIR)
            inventory.setItem(i, item)
        }
        GUIManager.listeningGUIs[inventory] = this
        return inventory.also { this.inventory = inventory }
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

    class GUIBuilder {
        internal var name: String = ""
        internal var size: Int = 27
            set(value) {
                var assert = value
                assert = (round(assert.toDouble() / 9) * 9).toInt()
                if (assert < 9) assert = 9
                if (assert > 54) assert = 54
                field = assert
            }
        internal var lock = true
        internal var persistent = false
        internal var slotMap = mutableMapOf<Int, Slot>()

        fun name(name: String) = apply { this.name = name }
        fun size(size: Int) = apply { this.size = size }
        fun lock(lock: Boolean) = apply { this.lock = lock }
        fun slot(slotIndex: Int, slot: Slot) = apply { this.slotMap[slotIndex] = slot }

        fun slot(slotIndices: List<Int>, slot: Slot) {
            slotIndices.forEach { i ->
                slot(i, slot)
            }
        }

        fun slot(slotIndices: IntRange, slot: Slot) {
            slot(slotIndices.toList(), slot)
        }


        fun build(): GUI {
            return GUI(this).also { it.toBukkit() }
        }

    }

}

/**
 * The Slot class
 *
 * The Slot class contains information as to what should go or happen to a slot.
 *
 * An [InventoryClickEvent] is forwarded to the action consumer, therefore you may use the event object as you wish.
 * This event is called with [EventPriority.LOWEST], meaning this will likely be the first instance of the event being handled
 *
 * @param item The [ItemStack] corresponding to the slot shown to the player
 * @param action The action taken when the slot is clicked by the player
 */
class Slot() {

    private constructor(builder: SlotBuilder) : this() {
        item = builder.item
        action = builder.action
    }

    var item: ItemStack = ItemStack.of(Material.AIR)
    var action: Consumer<InventoryClickEvent>? = null

    fun action(consumer: Consumer<InventoryClickEvent>) {
        action = consumer
    }

    class SlotBuilder {
        internal var item: ItemStack = ItemStack.of(Material.AIR)
        internal var action: Consumer<InventoryClickEvent>? = null

        fun item(item: ItemStack) = apply { this.item = item }
        fun action(action: Consumer<InventoryClickEvent>) = apply { this.action = action }
        fun build() = Slot(this)
    }

}

fun buildGUI(): GUI.GUIBuilder {
    return GUI.GUIBuilder()
}

fun buildSlot(): Slot.SlotBuilder {
    return Slot.SlotBuilder()
}

/**
 * With use of a lambda argument, [createGUI] allows for easy creation of a GUI (which is converted to an [Inventory] object)
 * When in the lambda, assuming you're using Kotlin
 */
fun createGUI(init: GUI.() -> Unit): GUI {
    val gui = GUI()
    gui.init()
    gui.toBukkit()
    return gui
}

fun editGUI(inventory: Inventory, init: GUI.() -> Unit) {
    editGUI(GUIManager.listeningGUIs[inventory]!!, init)
}

fun editGUI(gui: GUI, init: GUI.() -> Unit) {
    val newGUI = gui
    newGUI.init()
    gui.update(newGUI)
}