package com.deyo.hypixel.service.auction.endpoints;

import com.deyo.hypixel.commons.skyblock.auctions.AuctionCategories;
import com.deyo.hypixel.commons.skyblock.auctions.AuctionItem;
import com.deyo.hypixel.commons.skyblock.auctions.AuctionsFilter;
import com.deyo.hypixel.commons.skyblock.auctions.AuctionsSorting;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.skyblock.item.UnderstandableSkyBlockItem;
import com.deyo.hypixel.commons.skyblock.item.attribute.attributes.ItemAttributeType;
import com.deyo.hypixel.service.auction.AuctionsCacheService;
import net.minestom.server.item.Material;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuctionServiceLogicTest {

    @Test
    void creatingAnAuctionDocumentDoesNotRewriteTheItemType() {
        ItemAttributeType type = new ItemAttributeType();
        type.setValue("ASPECT_OF_THE_END");
        UnderstandableSkyBlockItem item = new UnderstandableSkyBlockItem(
                ItemType.AIR, List.of(type), 1, Material.AIR);
        AuctionItem auctionItem = new AuctionItem(item, UUID.randomUUID(),
                System.currentTimeMillis() + 60_000, false, 100L);

        Document document = EndpointAddItem.toDocument(auctionItem, AuctionCategories.WEAPONS);

        assertEquals("ASPECT_OF_THE_END", item.getAttribute("item_type").getValue());
        assertTrue(document.getString("item").contains("\"attr-item_type\":\"ASPECT_OF_THE_END\""),
                document.getString("item"));
        assertEquals(AuctionCategories.WEAPONS.name(), document.getString("category"));
    }

    @Test
    void binFilterSkipsDocumentsWithoutABinFlag() {
        List<Document> documents = List.of(
                new Document("_id", "bin").append("bin", true),
                new Document("_id", "auction").append("bin", false),
                new Document("_id", "legacy")
        );

        assertEquals(List.of("bin"), ids(AuctionsCacheService.applyFilter(documents, AuctionsFilter.BIN_ONLY)));
        assertEquals(List.of("auction"),
                ids(AuctionsCacheService.applyFilter(documents, AuctionsFilter.AUCTIONS_ONLY)));
        assertEquals(3, AuctionsCacheService.applyFilter(documents, AuctionsFilter.SHOW_ALL).size());
    }

    @Test
    void sortingHandlesEveryOrderingAndMissingPrices() {
        AuctionItem cheap = auction(100, 3_000, 1);
        AuctionItem expensive = auction(500, 1_000, 5);
        AuctionItem missingPrice = auction(null, 2_000, 0);

        List<AuctionItem> items = new ArrayList<>(List.of(cheap, expensive, missingPrice));

        EndpointFetchItems.sortItems(items, AuctionsSorting.HIGHEST_BID);
        assertEquals(List.of(expensive, cheap, missingPrice), items);

        EndpointFetchItems.sortItems(items, AuctionsSorting.LOWEST_BID);
        assertEquals(List.of(cheap, expensive, missingPrice), items);

        EndpointFetchItems.sortItems(items, AuctionsSorting.ENDING_SOON);
        assertEquals(List.of(expensive, missingPrice, cheap), items);

        EndpointFetchItems.sortItems(items, AuctionsSorting.MOST_BIDS);
        assertEquals(List.of(missingPrice, cheap, expensive), items);
    }

    private static List<String> ids(List<Document> documents) {
        return documents.stream().map(document -> document.getString("_id")).toList();
    }

    private static AuctionItem auction(Integer price, long end, int bids) {
        AuctionItem item = new AuctionItem();
        item.setUuid(UUID.randomUUID());
        item.setOriginator(UUID.randomUUID());
        item.setItem(new UnderstandableSkyBlockItem(
                ItemType.AIR,
                List.of(),
                1,
                Material.AIR));
        item.setStartingPrice(price);
        item.setEndTime(end);
        item.setBin(false);
        List<AuctionItem.Bid> bidList = new ArrayList<>();
        for (int index = 0; index < bids; index++) {
            bidList.add(new AuctionItem.Bid((long) index, UUID.randomUUID(), 10L));
        }
        item.setBids(bidList);
        return item;
    }
}
