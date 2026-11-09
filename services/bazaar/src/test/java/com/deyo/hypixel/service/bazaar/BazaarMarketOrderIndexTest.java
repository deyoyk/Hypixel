package com.deyo.hypixel.service.bazaar;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BazaarMarketOrderIndexTest {

    @Test
    void cancellingOwnSellOrderRemovesItFromTheBook() {
        BazaarMarket market = BazaarMarket.get();
        String item = uniqueItem();
        UUID owner = UUID.randomUUID();
        UUID profile = UUID.randomUUID();
        BazaarMarket.LimitOrder order = new BazaarMarket.LimitOrder(
                owner, profile, BazaarMarket.LimitOrder.Side.SELL, 10.0, 5.0);
        market.injectLoadedOrder(item, order);

        assertTrue(market.cancelOrder(order.orderId, owner, profile));
        assertTrue(market.getSellOrders(item).isEmpty());
    }

    @Test
    void cancellingAnotherPlayersOrderIsRejected() {
        BazaarMarket market = BazaarMarket.get();
        String item = uniqueItem();
        UUID owner = UUID.randomUUID();
        UUID profile = UUID.randomUUID();
        BazaarMarket.LimitOrder order = new BazaarMarket.LimitOrder(
                owner, profile, BazaarMarket.LimitOrder.Side.SELL, 10.0, 5.0);
        market.injectLoadedOrder(item, order);

        assertFalse(market.cancelOrder(order.orderId, UUID.randomUUID(), profile));
        assertEquals(1, market.getSellOrders(item).size());
    }

    @Test
    void cancellingAnUnknownOrderIsRejected() {
        assertFalse(BazaarMarket.get().cancelOrder(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()));
    }

    private static String uniqueItem() {
        return "TEST_ITEM_" + UUID.randomUUID();
    }
}
