package com.deyo.hypixel.type.skyblockgeneric.item.components;

import com.deyo.hypixel.commons.text.Text;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExtraRarityComponentTest {

    @Test
    void parsesMarkupIntoText() {
        ExtraRarityComponent component = new ExtraRarityComponent("<c>MYSTIC");

        assertEquals("MYSTIC", component.getExtraRarityDisplay(null).plain());
    }

    @Test
    void acceptsTextFunctions() {
        ExtraRarityComponent component = new ExtraRarityComponent(_ -> Text.of("<a>{}", "SHARD"));

        assertEquals("SHARD", component.getExtraRarityDisplay(null).plain());
    }
}
