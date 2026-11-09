package com.deyo.hypixel.commons.skyblock.item;

import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UnderstandableSkyBlockItemTest {

    @Test
    void attributesAreCopiedAndImmutable() {
        UnderstandableSkyBlockItem item = new UnderstandableSkyBlockItem(
                ItemType.COBBLESTONE, List.of(), 1, Material.COBBLESTONE);

        assertThrows(UnsupportedOperationException.class,
                () -> item.attributes().add(null));
        assertEquals(List.of(), item.attributes());
    }
}
