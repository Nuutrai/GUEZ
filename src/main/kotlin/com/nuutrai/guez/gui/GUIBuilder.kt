package com.nuutrai.guez.gui

import org.bukkit.inventory.ItemStack

class GUIBuilder {

    fun setSlot(slotIndex: Int): SlotBuilder {
        return SlotBuilder(slotIndex, this)
    }

    class SlotBuilder(slotIndex: Int, parent: GUIBuilder) {
        
        var item: ItemStack
        
        fun setItem(item: ItemStack): SlotBuilder {
            
        }
        
    }
    
}