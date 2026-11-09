package com.deyo.hypixel.service.darkauction;

import com.deyo.hypixel.commons.protocol.objects.darkauction.DarkAuctionEventProtocol;
import com.deyo.hypixel.commons.protocol.objects.darkauction.DarkAuctionEventPushProtocol;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DarkAuctionSchedulerTest {

    @Test
    void bidPlacedRequestHandlesAnEmptyAuction() {
        DarkAuctionState auction = new DarkAuctionState();
        auction.setCurrentBid(DarkAuctionState.STARTING_BID);

        DarkAuctionEventPushProtocol.Request request = DarkAuctionScheduler.buildRequest(
                DarkAuctionEventProtocol.EventType.BID_PLACED, auction, 10, null, 0);

        assertNull(request.newBidderId());
        assertEquals(DarkAuctionState.STARTING_BID, request.newBidAmount());
        assertEquals(auction.getAuctionId().toString(), request.auctionId());
    }

    @Test
    void bidPlacedRequestIncludesTheHighestBidder() {
        DarkAuctionState auction = new DarkAuctionState();
        UUID bidder = UUID.randomUUID();
        auction.setHighestBidderId(bidder);
        auction.setCurrentBid(100_000);

        DarkAuctionEventPushProtocol.Request request = DarkAuctionScheduler.buildRequest(
                DarkAuctionEventProtocol.EventType.BID_PLACED, auction, 10, null, 0);

        assertEquals(bidder.toString(), request.newBidderId());
        assertEquals(100_000, request.newBidAmount());
        assertEquals(bidder.toString(), request.highestBidderId());
    }

    @Test
    void nonBidEventsDoNotIncludeANewBidder() {
        DarkAuctionState auction = new DarkAuctionState();
        UUID bidder = UUID.randomUUID();
        auction.setHighestBidderId(bidder);
        auction.setCurrentBid(100_000);

        DarkAuctionEventPushProtocol.Request request = DarkAuctionScheduler.buildRequest(
                DarkAuctionEventProtocol.EventType.ROUND_START, auction, 10, null, 0);

        assertNull(request.newBidderId());
        assertEquals(0, request.newBidAmount());
    }
}
