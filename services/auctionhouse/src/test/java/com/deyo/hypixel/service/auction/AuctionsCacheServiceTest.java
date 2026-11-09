package com.deyo.hypixel.service.auction;

import com.deyo.hypixel.commons.skyblock.auctions.AuctionsFilter;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuctionsCacheServiceTest {

    @Test
    void filtersAuctionsByBinType() {
        List<Document> documents = List.of(
                new Document("bin", false),
                new Document("bin", true),
                new Document("bin", null)
        );

        assertEquals(3, AuctionsCacheService.applyFilter(documents, AuctionsFilter.SHOW_ALL).size());
        assertEquals(1, AuctionsCacheService.applyFilter(documents, AuctionsFilter.AUCTIONS_ONLY).size());
        assertEquals(1, AuctionsCacheService.applyFilter(documents, AuctionsFilter.BIN_ONLY).size());
    }

    @Test
    void cacheKeysAreNormalizedAndNamespaced() {
        assertEquals("docs:SWORD", AuctionsCacheService.categoryKey("sword"));
        assertEquals("auctions:SWORD:BIN_ONLY", AuctionsCacheService.auctionKey("sword", AuctionsFilter.BIN_ONLY));
    }
}
