package com.deyo.hypixel.service.auction.endpoints;

import com.deyo.hypixel.commons.skyblock.auctions.AuctionsSorting;
import com.deyo.hypixel.commons.protocol.objects.auctions.AuctionFetchItemsProtocol;
import com.deyo.hypixel.service.auction.AuctionService;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.commons.skyblock.auctions.AuctionItem;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class EndpointFetchItems implements RedisMessageHandler<
        AuctionFetchItemsProtocol.AuctionFetchItemsMessage,
        AuctionFetchItemsProtocol.AuctionFetchItemsResponse> {

    @Override
    public AuctionFetchItemsProtocol protocol() {
        return new AuctionFetchItemsProtocol();
    }

    @Override
    public AuctionFetchItemsProtocol.AuctionFetchItemsResponse handle(AuctionFetchItemsProtocol.AuctionFetchItemsMessage messageObject, RedisMessageContext context) {
        AuctionsSorting sorting = messageObject.sorting();
        List<Document> results = AuctionService.cacheService.getAuctions(
                messageObject.category().toString(), messageObject.filter());

        List<AuctionItem> items = new ArrayList<>(results.size());
        for (Document document : results) {
            try {
                items.add(AuctionItem.fromDocument(document));
            } catch (Exception exception) {
                Logger.error(exception, "Skipping unreadable auction _id={}", document.get("_id"));
            }
        }

        sortItems(items, sorting);

        return new AuctionFetchItemsProtocol.AuctionFetchItemsResponse(items, true, null);
    }

    static void sortItems(List<AuctionItem> items, AuctionsSorting sorting) {
        Comparator<AuctionItem> comparator = switch (sorting) {
            case HIGHEST_BID -> Comparator.comparing(AuctionItem::getStartingPrice,
                    Comparator.nullsLast(Comparator.reverseOrder()));
            case LOWEST_BID -> Comparator.comparing(AuctionItem::getStartingPrice,
                    Comparator.nullsLast(Comparator.naturalOrder()));
            case ENDING_SOON -> Comparator.comparingLong(AuctionItem::getEndTime);
            case MOST_BIDS -> Comparator.comparingInt(item -> item.getBids() == null ? 0 : item.getBids().size());
        };
        items.sort(comparator);
    }
}
