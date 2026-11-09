package com.deyo.hypixel.type.backwaterbayou.gui;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.type.skyblockgeneric.gui.ShopView;
import com.deyo.hypixel.type.skyblockgeneric.shop.type.CombinedShopPrice;
import com.deyo.hypixel.type.skyblockgeneric.shop.type.ItemShopPrice;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GUIJunkerJoelTest {

    @BeforeAll
    static void registerItemAttributes() {
        ItemAttribute.registerItemAttributes();
    }

    @Test
    void supportedTradesAreAttachedInSlotOrder() {
        GUIJunkerJoel gui = new GUIJunkerJoel();
        ShopView.State state = gui.initialState();

        assertEquals(List.of(
                ItemType.JUNK_SINKER,
                ItemType.TREASURE_BAIT,
                ItemType.CHALLENGE_ROD,
                ItemType.JUNK_TALISMAN,
                ItemType.JUNK_RING,
                ItemType.JUNK_ARTIFACT,
                ItemType.STINGY_SINKER,
                ItemType.SPEEDY_LINE,
                ItemType.BRONZE_SHIP_ENGINE,
                ItemType.GOLD_BOTTLE_CAP,
                ItemType.BAYOU_TRAVEL_SCROLL
        ), state.items().stream()
                .map(item -> item.getItem().getAttributeHandler().getPotentialType())
                .toList());

        ShopView.ShopItem treasureBait = state.items().get(1);
        assertEquals(16, treasureBait.getAmount());
        assertTrue(treasureBait.getPrice() instanceof ItemShopPrice);

        assertTrue(state.items().get(4).getPrice() instanceof CombinedShopPrice);
        assertTrue(state.items().get(9).getPrice() instanceof CombinedShopPrice);
    }
}
