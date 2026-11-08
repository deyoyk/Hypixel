package com.deyo.hypixel.service.orchestrator.endpoints;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.orchestrator.GetGameCountsProtocol;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.service.orchestrator.OrchestratorCache;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class GetGameCountsEndpoint implements RedisMessageHandler
        <GetGameCountsProtocol.GetGameCountsMessage,
                GetGameCountsProtocol.GetGameCountsResponse> {

    @Override
    public RedisProtocol<GetGameCountsProtocol.GetGameCountsMessage, GetGameCountsProtocol.GetGameCountsResponse> protocol() {
        return new GetGameCountsProtocol();
    }

    @Override
    public GetGameCountsProtocol.GetGameCountsResponse handle(GetGameCountsProtocol.GetGameCountsMessage body, RedisMessageContext context) {
        var stats = OrchestratorCache.getGameCounts(body.type(), body.gameTypeName(), body.mapName());
        return new GetGameCountsProtocol.GetGameCountsResponse(stats.playerCount(), stats.gameCount(), true, null);
    }
}
