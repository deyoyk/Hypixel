package com.deyo.hypixel.service.bazaar;

import com.deyo.hypixel.commons.skyblock.bazaar.BuyOrderRefundTransaction;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class BazaarMarketRefundTest {

    @Test
    void cancellingAnUnfilledBuyOrderRefundsTheWholeEscrow() {
        BazaarMarket.LimitOrder order = new BazaarMarket.LimitOrder(
                UUID.randomUUID(), UUID.randomUUID(), BazaarMarket.LimitOrder.Side.BUY, 100.0, 5.0);
        BazaarMarket.OrderRefundTracker tracker = new BazaarMarket.OrderRefundTracker(order, "ENCHANTED_DIAMOND");

        assertEquals(5.0, tracker.getRemainingQuantity(), 0.0001);
        assertEquals(500.0, tracker.getTotalRefundOwed(), 0.0001);
    }

    @Test
    void cancellingAPartiallyFilledBuyOrderRefundsEscrowAndPriceImprovement() {
        BazaarMarket.LimitOrder order = new BazaarMarket.LimitOrder(
                UUID.randomUUID(), UUID.randomUUID(), BazaarMarket.LimitOrder.Side.BUY, 100.0, 5.0);
        BazaarMarket.OrderRefundTracker tracker = new BazaarMarket.OrderRefundTracker(order, "ENCHANTED_DIAMOND");

        tracker.recordPartialFill(2.0, 80.0);

        assertEquals(3.0, tracker.getRemainingQuantity(), 0.0001);
        assertEquals(40.0, tracker.accumulatedRefunds, 0.0001);
        assertEquals(340.0, tracker.getTotalRefundOwed(), 0.0001);
    }

    @Test
    void pendingTransactionsCanParseBuyOrderRefunds() {
        UUID orderId = UUID.randomUUID();
        UUID owner = UUID.randomUUID();
        UUID profile = UUID.randomUUID();
        BuyOrderRefundTransaction refund = new BuyOrderRefundTransaction(
                orderId, "ENCHANTED_DIAMOND", owner, profile, 340.0, "CANCELLED", Instant.now());

        Document document = Document.parse(refund.toJSON().toString());
        var parsed = PendingTransactionsDatabase.parseTransactionFromDocument(
                "BuyOrderRefundTransaction", document);

        BuyOrderRefundTransaction parsedRefund = assertInstanceOf(BuyOrderRefundTransaction.class, parsed);
        assertEquals(orderId, parsedRefund.orderId());
        assertEquals(owner, parsedRefund.owner());
        assertEquals(340.0, parsedRefund.refundAmount(), 0.0001);
        assertEquals("CANCELLED", parsedRefund.reason());
    }
}
