package com.deyo.hypixel.service.friend.endpoints;

import com.deyo.hypixel.commons.presence.PresenceInfo;
import com.deyo.hypixel.commons.protocol.objects.presence.UpdatePresenceProtocol;
import com.deyo.hypixel.service.friend.FriendCache;
import com.deyo.hypixel.service.friend.PresenceStorage;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class UpdatePresenceEndpoint implements RedisMessageHandler<
        UpdatePresenceProtocol.UpdatePresenceMessage,
        UpdatePresenceProtocol.UpdatePresenceResponse> {

    @Override
    public UpdatePresenceProtocol protocol() {
        return new UpdatePresenceProtocol();
    }

    @Override
    public UpdatePresenceProtocol.UpdatePresenceResponse handle(UpdatePresenceProtocol.UpdatePresenceMessage messageObject, RedisMessageContext context) {

        PresenceInfo incoming = messageObject.presence();
        PresenceInfo previous = PresenceStorage.upsertPreservingServer(incoming);

        boolean stateChanged = previous == null || previous.isOnline() != incoming.isOnline();

        if (stateChanged) {
            String playerName = FriendCache.getPlayerName(incoming.getUuid());
            if (incoming.isOnline()) {
                FriendCache.handlePlayerJoin(incoming.getUuid(), playerName);
            } else {
                FriendCache.handlePlayerLeave(incoming.getUuid(), playerName);
            }
        }

        return new UpdatePresenceProtocol.UpdatePresenceResponse(true, null);
    }
}

