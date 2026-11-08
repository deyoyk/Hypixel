package com.deyo.hypixel.type.bedwarsgame.redis.service;

import com.deyo.hypixel.commons.bedwars.BedWarsGameType;
import com.deyo.hypixel.commons.bedwars.map.BedWarsMapsConfig;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.game.InstantiateGamePushProtocol;
import com.deyo.hypixel.commons.protocol.objects.game.InstantiateGamePushProtocol.Request;
import com.deyo.hypixel.commons.protocol.objects.game.InstantiateGamePushProtocol.Response;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.type.bedwarsgame.TypeBedWarsGameLoader;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TypedInstantiateGameHandler implements RedisMessageHandler<Request, Response> {

    private static final InstantiateGamePushProtocol PROTOCOL = new InstantiateGamePushProtocol();

    @Override
    public RedisProtocol<Request, Response> protocol() {
        return PROTOCOL;
    }

    @Override
    public Response handle(Request request, RedisMessageContext context) {
        try {
            BedWarsGameType gameType = BedWarsGameType.valueOf(request.gameType().toUpperCase());

            boolean hasRequestedMap = request.map() != null && !request.map().isEmpty();

            BedWarsMapsConfig.MapEntry mapEntry = null;
            if (TypeBedWarsGameLoader.getMapsConfig() != null) {
                List<BedWarsMapsConfig.MapEntry> maps = TypeBedWarsGameLoader.getMapsConfig().getMaps();
                if (hasRequestedMap) {
                    for (BedWarsMapsConfig.MapEntry entry : maps) {
                        if (entry.getId().equals(request.map()) || entry.getName().equals(request.map())) {
                            if (entry.getConfiguration() != null &&
                                    entry.getConfiguration().getTypes() != null &&
                                !entry.getConfiguration().getTypes().contains(gameType.getMapCompatibilityType())) {
                                return Response.failure("Map does not support game type: " + gameType);
                            }
                            mapEntry = entry;
                            break;
                        }
                    }
                } else {
                    // No specific map requested (queueing a mode): pick a random map that supports it.
                    List<BedWarsMapsConfig.MapEntry> compatible = maps.stream()
                            .filter(e -> e.getConfiguration() != null
                                    && e.getConfiguration().getTypes() != null
                                    && e.getConfiguration().getTypes().contains(gameType.getMapCompatibilityType()))
                            .toList();
                    if (!compatible.isEmpty()) {
                        mapEntry = compatible.get(ThreadLocalRandom.current().nextInt(compatible.size()));
                    }
                }
            }

            if (mapEntry == null) {
                return Response.failure(hasRequestedMap
                        ? "Map not found: " + request.map()
                        : "No compatible maps available for " + gameType);
            }

            BedWarsGame game = TypeBedWarsGameLoader.createGame(mapEntry, gameType);
            if (game == null) {
                return Response.failure("Server at capacity, cannot create new game");
            }

            return Response.success(game.getGameId(), mapEntry.getName(), gameType.toString());
        } catch (Exception e) {
            return Response.failure("Failed to instantiate game: " + e.getMessage());
        }
    }
}
