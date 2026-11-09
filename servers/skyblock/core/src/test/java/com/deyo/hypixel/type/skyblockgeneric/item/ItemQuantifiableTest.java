package com.deyo.hypixel.type.skyblockgeneric.item;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemQuantifiableTest {

    @BeforeAll
    static void registerAttributes() {
        ItemAttribute.registerItemAttributes();
    }

    @Test
    void cloneDoesNotShareTheStoredItem() {
        ItemQuantifiable original = new ItemQuantifiable(ItemType.COBBLESTONE, 5);
        ItemQuantifiable clone = original.clone();

        clone.setMaterial(ItemType.DIAMOND);

        assertEquals(ItemType.COBBLESTONE,
                original.getItem().getAttributeHandler().getPotentialType());
    }

    @Test
    void toSkyBlockItemDoesNotMutateTheStoredItem() {
        ItemQuantifiable original = new ItemQuantifiable(ItemType.COBBLESTONE, 5);

        SkyBlockItem exported = original.toSkyBlockItem();
        exported.setAmount(9);

        assertEquals(5, original.getItem().getAmount());
    }
}
