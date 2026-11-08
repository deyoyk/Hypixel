package com.deyo.hypixel.service.friend.endpoints;

import com.deyo.hypixel.commons.protocol.objects.friend.AreFriendsProtocol;
import com.deyo.hypixel.service.friend.FriendCache;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class AreFriendsEndpoint implements RedisMessageHandler<
        AreFriendsProtocol.AreFriendsMessage,
        AreFriendsProtocol.AreFriendsResponse> {

    @Override
    public AreFriendsProtocol protocol() {
        return new AreFriendsProtocol();
    }

    @Override
    public AreFriendsProtocol.AreFriendsResponse handle(AreFriendsProtocol.AreFriendsMessage messageObject, RedisMessageContext context) {

        boolean areFriends = FriendCache.areFriends(messageObject.player(), messageObject.other());
        return new AreFriendsProtocol.AreFriendsResponse(areFriends, true, null);
    }
}
