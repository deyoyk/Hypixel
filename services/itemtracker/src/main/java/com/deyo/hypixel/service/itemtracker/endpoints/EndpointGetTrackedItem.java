package com.deyo.hypixel.service.itemtracker.endpoints;

import com.deyo.hypixel.commons.TrackedItem;
import com.deyo.hypixel.commons.protocol.objects.itemtracker.TrackedItemRetrieveProtocol;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.service.itemtracker.TrackedItemsDatabase;

import java.util.UUID;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class EndpointGetTrackedItem implements RedisMessageHandler<
        TrackedItemRetrieveProtocol.TrackedItemRetrieveMessage,
        TrackedItemRetrieveProtocol.TrackedItemResponse> {

    @Override
    public TrackedItemRetrieveProtocol protocol() {
        return new TrackedItemRetrieveProtocol();
    }

    @Override
    public TrackedItemRetrieveProtocol.TrackedItemResponse handle(TrackedItemRetrieveProtocol.TrackedItemRetrieveMessage messageObject, RedisMessageContext context) {
        UUID itemUUID = messageObject.itemUUID();

        if (!new TrackedItemsDatabase(itemUUID).exists()) {
            throw new RuntimeException("Attempted to get a tracked item that does not exist.");
        }

        TrackedItem item = new TrackedItemsDatabase(itemUUID).get();
        return new TrackedItemRetrieveProtocol.TrackedItemResponse(item, true, null);
    }
}