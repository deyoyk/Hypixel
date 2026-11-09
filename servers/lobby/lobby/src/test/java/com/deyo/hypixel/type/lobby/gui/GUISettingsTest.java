package com.deyo.hypixel.type.lobby.gui;

import net.minestom.server.inventory.InventoryType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GUISettingsTest {

    @Test
    void settingsGuiIsUsable() {
        GUISettings gui = new GUISettings();

        assertNotNull(gui.getTitle());
        assertEquals(InventoryType.CHEST_4_ROW, gui.getSize());
    }

    @Test
    void visibilityLabelReflectsState() {
        assertEquals("<a>Shown", GUISettings.visibilityLabel(true));
        assertEquals("<c>Hidden", GUISettings.visibilityLabel(false));
    }
}
