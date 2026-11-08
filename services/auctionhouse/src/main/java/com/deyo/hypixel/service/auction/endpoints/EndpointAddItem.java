package com.deyo.hypixel.service.auction.endpoints;

import com.deyo.hypixel.commons.skyblock.auctions.AuctionCategories;
import com.deyo.hypixel.commons.skyblock.auctions.AuctionItem;
import com.deyo.hypixel.commons.skyblock.item.UnderstandableSkyBlockItem;
import com.deyo.hypixel.commons.protocol.objects.auctions.AuctionAddItemProtocol;
import com.deyo.hypixel.service.auction.AuctionActiveDatabase;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import org.bson.Document;

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
        UnderstandableSkyBlockItem item = auctionItem.getItem();
        item.getAttribute("item_type").setValue("HYPERION");

        AuctionCategories category = messageObject.category();
        Document document = auctionItem.toDocument();
        document.put("category", category.name());

        Thread.startVirtualThread(() -> {
            if (AuctionActiveDatabase.collection.find(new Document("_id", document.get("_id"))).first() != null) {
                AuctionActiveDatabase.collection.replaceOne(
                        new Document("_id", document.get("_id")),
                        document
                );
            } else {
                AuctionActiveDatabase.collection.insertOne(document);
            }
        });

        return new AuctionAddItemProtocol.AuctionAddItemResponse(UUID.fromString((String) document.get("_id")), true, null);
    }
}
