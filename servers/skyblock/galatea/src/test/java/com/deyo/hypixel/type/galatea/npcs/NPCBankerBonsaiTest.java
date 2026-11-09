package com.deyo.hypixel.type.galatea.npcs;

import net.minestom.server.inventory.InventoryType;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.banker.GUIBanker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NPCBankerBonsaiTest {

    @Test
    void bankerGuiIsUsable() {
        GUIBanker gui = NPCBankerBonsai.bankerGui();

        assertNotNull(gui.getTitle());
        assertEquals(InventoryType.CHEST_4_ROW, gui.getSize());
    }
}
