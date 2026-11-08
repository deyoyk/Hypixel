package com.deyo.hypixel.service.friend.endpoints;

import com.deyo.hypixel.commons.friend.PendingFriendRequest;
import com.deyo.hypixel.commons.protocol.objects.friend.GetPendingFriendRequestsProtocol;
import com.deyo.hypixel.service.friend.FriendCache;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.List;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class GetPendingRequestsEndpoint implements RedisMessageHandler<
        GetPendingFriendRequestsProtocol.GetPendingRequestsMessage,
        GetPendingFriendRequestsProtocol.GetPendingRequestsResponse> {

    @Override
    public GetPendingFriendRequestsProtocol protocol() {
        return new GetPendingFriendRequestsProtocol();
    }

    @Override
    public GetPendingFriendRequestsProtocol.GetPendingRequestsResponse handle(GetPendingFriendRequestsProtocol.GetPendingRequestsMessage messageObject, RedisMessageContext context) {

        List<PendingFriendRequest> requests = FriendCache.getPendingRequestsFor(messageObject.playerUuid());
        return new GetPendingFriendRequestsProtocol.GetPendingRequestsResponse(requests, true, null);
    }
}
