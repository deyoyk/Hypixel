package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.levels.starter;

import com.deyo.hypixel.commons.text.Text;
import org.junit.jupiter.api.Test;

class GUIStarterAccessoriesTest {

    @Test
    void loreReflectsWhetherTheAccessoryIsCollected() {
        String collected = GUIStarterAccessories.accessoryLore(true).stream()
                .map(Text::serialize)
                .reduce("", String::concat);

        String missing = GUIStarterAccessories.accessoryLore(false).stream()
                .map(Text::serialize)
                .reduce("", String::concat);

        org.junit.jupiter.api.Assertions.assertTrue(collected.contains("Collected"), collected);
        org.junit.jupiter.api.Assertions.assertTrue(missing.contains("Not collected"), missing);
    }
}
