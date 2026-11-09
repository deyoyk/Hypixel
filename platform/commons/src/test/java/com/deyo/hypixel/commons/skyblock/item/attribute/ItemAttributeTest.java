package com.deyo.hypixel.commons.skyblock.item.attribute;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemAttributeTest {

    @Test
    void registrationCachesConstructorsAndStillReturnsFreshInstances() {
        ItemAttribute.registerItemAttributes();

        Collection<ItemAttribute> first = ItemAttribute.getPossibleAttributes();
        Collection<ItemAttribute> second = ItemAttribute.getPossibleAttributes();

        assertTrue(first.size() > 0);
        assertEquals(first.size(), second.size());
        ItemAttribute firstInstance = first.iterator().next();
        ItemAttribute secondInstance = second.iterator().next();
        assertEquals(firstInstance.getClass(), secondInstance.getClass());
        assertNotSame(firstInstance, secondInstance);
    }
}
