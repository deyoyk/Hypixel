package com.deyo.hypixel.service.auction.endpoints;

import com.deyo.hypixel.commons.skyblock.auctions.AuctionItem;
import com.deyo.hypixel.commons.protocol.objects.auctions.AuctionFetchItemProtocol;
import com.deyo.hypixel.service.auction.AuctionActiveDatabase;
import com.deyo.hypixel.service.auction.AuctionInactiveDatabase;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import org.bson.Document;

import java.util.UUID;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class EndpointFetchItem implements RedisMessageHandler<
        AuctionFetchItemProtocol.AuctionFetchItemMessage,
        AuctionFetchItemProtocol.AuctionFetchItemResponse> {

    @Override
    public AuctionFetchItemProtocol protocol() {
        return new AuctionFetchItemProtocol();
    }

    @Override
    public AuctionFetchItemProtocol.AuctionFetchItemResponse handle(AuctionFetchItemProtocol.AuctionFetchItemMessage messageObject, RedisMessageContext context) {
        UUID uuidToFetch = messageObject.uuid();

        AuctionItem toReturn = new AuctionItem();

        Document item = AuctionActiveDatabase.collection.find(new Document("_id", uuidToFetch.toString())).first();
        if (item != null) {
            toReturn = AuctionItem.fromDocument(item);
        }

        Document inactiveItem = AuctionInactiveDatabase.collection.find(new Document("_id", uuidToFetch.toString())).first();
        if (inactiveItem != null) {
            toReturn = AuctionItem.fromDocument(inactiveItem);
        }

        return new AuctionFetchItemProtocol.AuctionFetchItemResponse(toReturn, true, null);
    }
}
