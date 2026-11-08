package com.deyo.hypixel.service.friend.endpoints;

import com.deyo.hypixel.commons.friend.FriendData;
import com.deyo.hypixel.commons.protocol.objects.friend.GetFriendDataProtocol;
import com.deyo.hypixel.service.friend.FriendCache;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class GetFriendDataEndpoint implements RedisMessageHandler<
        GetFriendDataProtocol.GetFriendDataMessage,
        GetFriendDataProtocol.GetFriendDataResponse> {

    @Override
    public GetFriendDataProtocol protocol() {
        return new GetFriendDataProtocol();
    }

    @Override
    public GetFriendDataProtocol.GetFriendDataResponse handle(GetFriendDataProtocol.GetFriendDataMessage messageObject, RedisMessageContext context) {

        FriendData data = FriendCache.getFriendData(messageObject.playerUuid());
        return new GetFriendDataProtocol.GetFriendDataResponse(data, true, null);
    }
}
