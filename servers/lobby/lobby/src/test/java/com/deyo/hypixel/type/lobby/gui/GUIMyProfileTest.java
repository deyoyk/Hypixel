package com.deyo.hypixel.type.lobby.gui;

import net.minestom.server.inventory.InventoryType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GUIMyProfileTest {

    @Test
    void profileGuiIsUsable() {
        GUIMyProfile gui = new GUIMyProfile();

        assertNotNull(gui.getTitle());
        assertEquals(InventoryType.CHEST_6_ROW, gui.getSize());
    }
}
