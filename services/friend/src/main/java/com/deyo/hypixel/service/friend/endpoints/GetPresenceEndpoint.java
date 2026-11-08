package com.deyo.hypixel.service.friend.endpoints;

import com.deyo.hypixel.commons.presence.PresenceInfo;
import com.deyo.hypixel.commons.protocol.objects.presence.GetPresenceBulkProtocol;
import com.deyo.hypixel.service.friend.PresenceStorage;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.List;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class GetPresenceEndpoint implements RedisMessageHandler<
        GetPresenceBulkProtocol.GetPresenceBulkMessage,
        GetPresenceBulkProtocol.GetPresenceBulkResponse> {

    @Override
    public GetPresenceBulkProtocol protocol() {
        return new GetPresenceBulkProtocol();
    }

    @Override
    public GetPresenceBulkProtocol.GetPresenceBulkResponse handle(GetPresenceBulkProtocol.GetPresenceBulkMessage messageObject, RedisMessageContext context) {

        List<PresenceInfo> presence = PresenceStorage.getBulk(messageObject.uuids());
        return new GetPresenceBulkProtocol.GetPresenceBulkResponse(presence, true, null);
    }
}

