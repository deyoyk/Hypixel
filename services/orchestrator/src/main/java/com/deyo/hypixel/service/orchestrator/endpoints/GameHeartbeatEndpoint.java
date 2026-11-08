package com.deyo.hypixel.service.orchestrator.endpoints;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.orchestrator.GameHeartbeatProtocol;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.service.orchestrator.OrchestratorCache;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class GameHeartbeatEndpoint implements RedisMessageHandler
        <GameHeartbeatProtocol.HeartbeatMessage,
                GameHeartbeatProtocol.HeartbeatResponse> {

    @Override
    public RedisProtocol<GameHeartbeatProtocol.HeartbeatMessage, GameHeartbeatProtocol.HeartbeatResponse> protocol() {
        return new GameHeartbeatProtocol();
    }

    @Override
    public GameHeartbeatProtocol.HeartbeatResponse handle(GameHeartbeatProtocol.HeartbeatMessage body, RedisMessageContext context) {
        OrchestratorCache.handleHeartbeat(
            body.uuid(),
            body.shortName(),
            body.type(),
            body.maxPlayers(),
            body.onlinePlayers(),
            body.games(),
            body.mapAdvertisements(),
            body.remainingGameSlots()
        );
        return new GameHeartbeatProtocol.HeartbeatResponse(true, null);
    }
}