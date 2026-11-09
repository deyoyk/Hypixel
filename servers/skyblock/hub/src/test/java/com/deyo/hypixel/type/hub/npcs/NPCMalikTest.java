package com.deyo.hypixel.type.hub.npcs;

import net.minestom.server.inventory.InventoryType;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.GUIReforge;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NPCMalikTest {

    @Test
    void reforgeGuiIsUsable() {
        GUIReforge gui = NPCMalik.reforgeGui();

        assertNotNull(gui.getTitle());
        assertEquals(InventoryType.CHEST_5_ROW, gui.getSize());
    }
}
