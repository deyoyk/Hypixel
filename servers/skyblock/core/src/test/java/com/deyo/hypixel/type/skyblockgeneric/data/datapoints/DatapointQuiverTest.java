package com.deyo.hypixel.type.skyblockgeneric.data.datapoints;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class DatapointQuiverTest {

    @BeforeAll
    static void registerAttributes() {
        ItemAttribute.registerItemAttributes();
    }

    @Test
    void firstItemUsesTheLowestOccupiedSlot() {
        SkyBlockItem highSlot = new SkyBlockItem(ItemType.COBBLESTONE);
        SkyBlockItem lowSlot = new SkyBlockItem(ItemType.DIAMOND);
        DatapointQuiver.PlayerQuiver quiver = new DatapointQuiver.PlayerQuiver(new HashMap<>());
        quiver.getQuiverMap().put(10, highSlot);
        quiver.getQuiverMap().put(2, lowSlot);

        assertSame(lowSlot, quiver.getFirstItemInQuiver());
    }

    @Test
    void consumingUsesAndRemovesTheLowestOccupiedSlot() {
        SkyBlockItem slotTwo = new SkyBlockItem(ItemType.DIAMOND, 2);
        SkyBlockItem slotTen = new SkyBlockItem(ItemType.COBBLESTONE, 1);
        DatapointQuiver.PlayerQuiver quiver = new DatapointQuiver.PlayerQuiver(new HashMap<>());
        quiver.getQuiverMap().put(10, slotTen);
        quiver.getQuiverMap().put(2, slotTwo);

        assertSame(slotTwo, quiver.consumeFirstItem());
        assertEquals(1, slotTwo.getAmount());
        assertEquals(2, quiver.getFirstItemSlot());

        assertSame(slotTwo, quiver.consumeFirstItem());
        assertEquals(10, quiver.getFirstItemSlot());
        assertSame(slotTen, quiver.getFirstItemInQuiver());

        assertSame(slotTen, quiver.consumeFirstItem());
        assertNull(quiver.consumeFirstItem());
    }
}
