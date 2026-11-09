package com.deyo.hypixel.type.hub.gui;

import net.minestom.server.inventory.InventoryType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GUIBiblioTest {

    @Test
    void wikiGuiIsUsable() {
        GUIBiblio gui = new GUIBiblio();

        assertNotNull(gui.getTitle());
        assertEquals(InventoryType.CHEST_4_ROW, gui.getSize());
    }
}
