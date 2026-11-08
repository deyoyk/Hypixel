package com.deyo.hypixel.service.orchestrator.endpoints;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.orchestrator.GetMapsProtocol;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.service.orchestrator.OrchestratorCache;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class GetMapsEndpoint implements RedisMessageHandler
        <GetMapsProtocol.GetMapsMessage,
                GetMapsProtocol.GetMapsResponse> {

    @Override
    public RedisProtocol<GetMapsProtocol.GetMapsMessage, GetMapsProtocol.GetMapsResponse> protocol() {
        return new GetMapsProtocol();
    }

    @Override
    public GetMapsProtocol.GetMapsResponse handle(GetMapsProtocol.GetMapsMessage body, RedisMessageContext context) {
        var maps = OrchestratorCache.getMaps(body.type(), body.mode());
        return new GetMapsProtocol.GetMapsResponse(maps.stream().sorted().toList(), true, null);
    }
}