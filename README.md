# Welcome To GU<i>EZ</i>!

GU<i>EZ</i> makes it easy to create GUIs for Minecraft in both Kotlin and Java

To implement GU<i>EZ</i>, add Jitpack to your repositories \
Then add the dependency `com.nuutrai:guez:1.0.2`

Here's an example of how to use GU<i>EZ</i> in Kotlin:
```kt
fun awesomeGUI() {
    val inv = createGUI {

        name = "My awesome GUI" // Defaults to ""
        size = 26 // Set to 27 internally to ensure a proper inventory
        lock = true // Defaults to true
        
        open { event -> // Customize the open event
            event.player.sendMessage("You opened my awesome GUI!")
        }
        close { event -> // And the close event
            event.player.sendMessage("Until next time!")
        }
        
        slot(27) { // Slot accepts an integer
            item = ItemStack.of(Material.BUCKET)
        }
        slot(0..5) { // A range
            item = ItemStack.of(Material.DIAMOND) // This accepts any item; doesn't care about lore, name etc.
            action { event -> // Customizes what happens when a player clicks on the item
                event.viewers[0].sendMessage("You clicked ${event.slot}!")
			}
        }
        slot(listOf(6, 10)) { // And a list
            item = ItemStack.of(Material.BEDROCK)
        }
    }

    editGUI(inv) { // You can also edit! Unless you change the name or size of the GUI, the GUI is not recreated, just updated
        slot(27) {
            item = ItemStack.of(Material.LAVA_BUCKET)
        }
    }

}
```

And here's Java!

```java

public void awesomeGUI() {
    GUI inv = buildGUI()

        .name("My awesome GUI")
        .size(26)
        .lock(true)

        .open(event -> {
            event.getPlayer().sendMessage("You opened my aweseome GUI!");
        })
        .close(event -> {
            event.getPlayer().sendMessage("Until next time!");
        })

        .slot(27,
            buildSlot()
                .item(ItemStack.of(Material.BUCKET))
                .build())
        // Unfortunately the range feature is not in Java
        .slot(Arrays.asList(6, 10),
            buildSlot()
                .item(ItemStack.of(Material.BEDROCK))
                .build())

        .build();

    editGUI(inv, buildGUI()
        .slot(27,
            buildSlot()
                .item(ItemStack.of(Material.LAVA_BUCKET))
                .build())
    );
}

```
If there's any bugs, let me know in the issues tab, or message me on Discord @Nuutrai <3