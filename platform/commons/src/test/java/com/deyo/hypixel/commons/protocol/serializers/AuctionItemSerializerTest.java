package com.deyo.hypixel.commons.protocol.serializers;

import com.deyo.hypixel.commons.skyblock.auctions.AuctionItem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuctionItemSerializerTest {

    @Test
    void clonePreservesIdentityAndCopiesBidHistory() {
        AuctionItem item = new AuctionItem();
        item.setUuid(UUID.randomUUID());
        item.setOriginator(UUID.randomUUID());
        item.setEndTime(123L);
        item.setBin(true);
        item.setStartingPrice(456);
        item.setBids(new ArrayList<>(List.of(
                new AuctionItem.Bid(1L, UUID.randomUUID(), 100L))));

        AuctionItem clone = new AuctionItemSerializer<AuctionItem>().clone(item);

        assertEquals(item.getUuid(), clone.getUuid());
        assertEquals(item.getOriginator(), clone.getOriginator());
        assertEquals(item.getEndTime(), clone.getEndTime());
        assertEquals(item.isBin(), clone.isBin());
        assertEquals(item.getStartingPrice(), clone.getStartingPrice());
        assertEquals(item.getBids(), clone.getBids());

        clone.getBids().add(new AuctionItem.Bid(2L, UUID.randomUUID(), 200L));
        assertEquals(1, item.getBids().size());
    }
}
