package com.deyo.hypixel.service.orchestrator.endpoints;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.UnderstandableProxyServer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.orchestrator.RejoinGameProtocol;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.service.orchestrator.OrchestratorCache;
import org.tinylog.Logger;

import java.util.ArrayList;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class RejoinGameEndpoint implements RedisMessageHandler<
        RejoinGameProtocol.RejoinGameRequest,
        RejoinGameProtocol.RejoinGameResponse> {

    @Override
    public RedisProtocol<RejoinGameProtocol.RejoinGameRequest, RejoinGameProtocol.RejoinGameResponse> protocol() {
        return new RejoinGameProtocol();
    }

    @Override
    public RejoinGameProtocol.RejoinGameResponse handle(RejoinGameProtocol.RejoinGameRequest body, RedisMessageContext context) {
        try {
            // Find the game this player is part of (active or disconnected)
            OrchestratorCache.GameWithServer gameWithServer = OrchestratorCache.findPlayerGame(body.playerUuid());

            if (gameWithServer == null) {
                return new RejoinGameProtocol.RejoinGameResponse(false, null, null, null, null, false, true, null);
            }

            OrchestratorCache.GameServerState hostingServer = OrchestratorCache.getServerByUuid(gameWithServer.serverUuid());
            if (hostingServer == null) {
                return new RejoinGameProtocol.RejoinGameResponse(false, null, null, null, null, false, true, null);
            }

            // Skywars does not support rejoining
            if (hostingServer.type() == ServerType.SKYWARS_GAME) {
                return new RejoinGameProtocol.RejoinGameResponse(false, null, null, null, null, false, true, null);
            }

            // Check if this player is in the disconnected list (meaning they should rejoin)
            // If they're in involvedPlayers, they're already in the game
            boolean isDisconnected = gameWithServer.game().getDisconnectedPlayers() != null
                    && gameWithServer.game().getDisconnectedPlayers().contains(body.playerUuid());

            if (!isDisconnected) {
                // Player is already in an active game, not a rejoin scenario
                return new RejoinGameProtocol.RejoinGameResponse(false, null, null, null, null, false, true, null);
            }

            UnderstandableProxyServer proxy = new UnderstandableProxyServer(
                    hostingServer.shortName(),
                    hostingServer.uuid(),
                    hostingServer.type(),
                    -1,
                    new ArrayList<>(),
                    hostingServer.maxPlayers(),
                    hostingServer.shortName()
            );

            return new RejoinGameProtocol.RejoinGameResponse(
                    true,
                    proxy,
                    gameWithServer.game().getGameId().toString(),
                    gameWithServer.game().getMap(),
                    null,
                    false,
                    true,
                    null
            );
        } catch (Exception e) {
            Logger.error(e, "Failed to check rejoin");
            return new RejoinGameProtocol.RejoinGameResponse(false, null, null, null, null, false, true, null);
        }
    }
}
