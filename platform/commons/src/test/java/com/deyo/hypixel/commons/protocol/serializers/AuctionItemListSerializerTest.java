package com.deyo.hypixel.commons.protocol.serializers;

import com.deyo.hypixel.commons.skyblock.auctions.AuctionItem;
import com.deyo.hypixel.commons.skyblock.item.UnderstandableSkyBlockItem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuctionItemListSerializerTest {

    @Test
    void serializesAndDeserializesAuctionItems() {
        AuctionItem item = new AuctionItem();
        item.setUuid(UUID.randomUUID());
        item.setOriginator(UUID.randomUUID());
        item.setItem(new UnderstandableSkyBlockItem());
        item.setEndTime(123L);
        item.setBin(false);
        item.setStartingPrice(100);
        item.setBids(new ArrayList<>(List.of(
                new AuctionItem.Bid(1L, UUID.randomUUID(), 50L))));

        AuctionItemListSerializer serializer = new AuctionItemListSerializer();
        List<AuctionItem> restored = serializer.deserialize(serializer.serialize(List.of(item)));

        assertEquals(1, restored.size());
        assertEquals(item.getUuid(), restored.getFirst().getUuid());
        assertEquals(item.getOriginator(), restored.getFirst().getOriginator());
        assertEquals(item.getEndTime(), restored.getFirst().getEndTime());
        assertEquals(item.isBin(), restored.getFirst().isBin());
        assertEquals(item.getStartingPrice(), restored.getFirst().getStartingPrice());
        assertEquals(item.getBids(), restored.getFirst().getBids());
    }
}
