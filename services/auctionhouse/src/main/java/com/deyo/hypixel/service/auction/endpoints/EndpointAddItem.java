package com.deyo.hypixel.service.auction.endpoints;

import com.deyo.hypixel.commons.skyblock.auctions.AuctionCategories;
import com.deyo.hypixel.commons.skyblock.auctions.AuctionItem;
import com.deyo.hypixel.commons.protocol.objects.auctions.AuctionAddItemProtocol;
import com.deyo.hypixel.service.auction.AuctionActiveDatabase;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.tinylog.Logger;

import java.util.UUID;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class EndpointAddItem implements RedisMessageHandler<
        AuctionAddItemProtocol.AuctionAddItemMessage,
        AuctionAddItemProtocol.AuctionAddItemResponse> {

    @Override
    public AuctionAddItemProtocol protocol() {
        return new AuctionAddItemProtocol();
    }

    @Override
    public AuctionAddItemProtocol.AuctionAddItemResponse handle(AuctionAddItemProtocol.AuctionAddItemMessage messageObject, RedisMessageContext context) {
        AuctionItem auctionItem = messageObject.item();
        if (auctionItem == null || auctionItem.getItem() == null || auctionItem.getUuid() == null) {
            return new AuctionAddItemProtocol.AuctionAddItemResponse(null, false, "Missing auction item");
        }

        Document document = toDocument(auctionItem, messageObject.category());
        try {
            AuctionActiveDatabase.collection.replaceOne(
                    new Document("_id", document.get("_id")),
                    document,
                    new ReplaceOptions().upsert(true)
            );
        } catch (RuntimeException exception) {
            Logger.error(exception, "Failed to persist auction item {}", auctionItem.getUuid());
            return new AuctionAddItemProtocol.AuctionAddItemResponse(auctionItem.getUuid(), false, "Failed to persist auction item");
        }

        return new AuctionAddItemProtocol.AuctionAddItemResponse(auctionItem.getUuid(), true, null);
    }

    static Document toDocument(AuctionItem auctionItem, AuctionCategories category) {
        Document document = auctionItem.toDocument();
        document.put("category", category.name());
        return document;
    }
}
