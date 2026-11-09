package com.deyo.hypixel.service.itemtracker.endpoints;

import com.deyo.hypixel.commons.TrackedItem;
import com.deyo.hypixel.commons.protocol.objects.itemtracker.TrackedItemUpdateProtocol;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.service.itemtracker.TrackedItemsDatabase;

import java.util.UUID;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class EndpointUpdateItem implements RedisMessageHandler<
        TrackedItemUpdateProtocol.TrackedItemUpdateMessage,
        TrackedItemUpdateProtocol.TrackedItemUpdateResponse> {

    @Override
    public TrackedItemUpdateProtocol protocol() {
        return new TrackedItemUpdateProtocol();
    }

    @Override
    public TrackedItemUpdateProtocol.TrackedItemUpdateResponse handle(TrackedItemUpdateProtocol.TrackedItemUpdateMessage messageObject, RedisMessageContext context) {
        UUID itemUUID = messageObject.itemUUID();
        UUID attachedPlayerUUID = messageObject.attachedPlayerUUID();
        UUID attachedPlayerProfile = messageObject.attachedPlayerProfile();
        String itemType = messageObject.itemType();

        Thread.startVirtualThread(() -> TrackedItemsDatabase.withItemLock(itemUUID, () -> {
            TrackedItemsDatabase database = new TrackedItemsDatabase(itemUUID);
            TrackedItem item = database.get();
            if (item == null) {
                item = TrackedItem.newTrackedItem(
                        itemUUID,
                        attachedPlayerUUID,
                        attachedPlayerProfile,
                        itemType,
                        TrackedItemsDatabase.getNumberMade(itemType)
                );
            } else {
                item.addOrUpdateAttachedPlayer(attachedPlayerUUID, attachedPlayerProfile);
            }
            database.insertOrUpdate(item);
            return null;
        }));

        return new TrackedItemUpdateProtocol.TrackedItemUpdateResponse();
    }
}
