package com.deyo.hypixel.type.skyblockgeneric.abiphone.impl;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.collection.CollectionCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbiphoneWalterTest {

    @Test
    void collectionRequirementUsesTheRequestedTier() {
        CollectionCategory.ItemCollection collection = new CollectionCategory.ItemCollection(
                ItemType.SULPHUR,
                new CollectionCategory.ItemCollectionReward(10),
                new CollectionCategory.ItemCollectionReward(25),
                new CollectionCategory.ItemCollectionReward(50)
        );

        assertFalse(AbiphoneWalter.meetsCollectionRequirement(49, collection, 3));
        assertTrue(AbiphoneWalter.meetsCollectionRequirement(50, collection, 3));
        assertFalse(AbiphoneWalter.meetsCollectionRequirement(50, collection, 4));
        assertFalse(AbiphoneWalter.meetsCollectionRequirement(50, null, 1));
    }
}
