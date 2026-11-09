package com.deyo.hypixel.type.lobby.gui;

import net.minestom.server.inventory.InventoryType;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointLocale;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GUILanguageSelectionTest {

    @Test
    void selectableLocalesExcludeTheUnsetMarker() {
        List<DatapointLocale.SupportedLocale> locales = GUILanguageSelection.selectableLocales();

        assertFalse(locales.contains(DatapointLocale.SupportedLocale.UNSET));
        assertTrue(locales.contains(DatapointLocale.SupportedLocale.ENGLISH));
        assertTrue(locales.contains(DatapointLocale.SupportedLocale.FINNISH));
    }

    @Test
    void localeDetectionMatchesSupportedLanguages() {
        assertEquals(DatapointLocale.SupportedLocale.FINNISH,
                DatapointLocale.SupportedLocale.fromLocale(Locale.forLanguageTag("fi-FI")));
        assertEquals(DatapointLocale.SupportedLocale.ENGLISH,
                DatapointLocale.SupportedLocale.fromLocale(Locale.US));
        assertEquals(DatapointLocale.SupportedLocale.ENGLISH,
                DatapointLocale.SupportedLocale.fromLocale(Locale.GERMANY));
    }

    @Test
    void languageGuiIsUsable() {
        GUILanguageSelection gui = new GUILanguageSelection();

        assertNotNull(gui.getTitle());
        assertEquals(InventoryType.CHEST_4_ROW, gui.getSize());
    }
}
