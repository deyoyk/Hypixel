package com.deyo.hypixel.type.skyblockgeneric.item;

import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigurableSkyBlockItemTest {

    @Test
    void loreIsCopiedAndImmutable() {
        List<String> source = new ArrayList<>(List.of("line"));
        ConfigurableSkyBlockItem item = new ConfigurableSkyBlockItem(
                "test", Material.STONE, source, new HashMap<>());

        source.add("other");

        assertEquals(List.of("line"), item.getLore());
        assertThrows(UnsupportedOperationException.class, () -> item.getLore().add("more"));
    }
}
