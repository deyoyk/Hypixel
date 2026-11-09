package com.deyo.hypixel.commons.protocol.serializers;

import com.deyo.hypixel.commons.skyblock.PlayerShopData;
import com.deyo.hypixel.commons.skyblock.item.UnderstandableSkyBlockItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerShopDataSerializerTest {

    @Test
    void clonePreservesDuplicateBuybacksInOrder() {
        PlayerShopData original = new PlayerShopData();
        original.pushBuyback(new UnderstandableSkyBlockItem(), 1);
        original.pushBuyback(new UnderstandableSkyBlockItem(), 2);

        PlayerShopData clone = new PlayerShopDataSerializer().clone(original);

        assertEquals(2, clone.popBuyback().getValue());
        assertEquals(1, clone.popBuyback().getValue());
    }

    @Test
    void cloneHasIndependentBuybackState() {
        PlayerShopData original = new PlayerShopData();
        original.pushBuyback(new UnderstandableSkyBlockItem(), 3);

        PlayerShopData clone = new PlayerShopDataSerializer().clone(original);
        clone.popBuyback();

        assertTrue(original.hasAnythingToBuyback());
        assertEquals(3, original.lastBuyback().getValue());
    }
}
