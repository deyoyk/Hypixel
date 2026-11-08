package com.deyo.hypixel.type.ravengarddungeon.redis;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.game.InstantiateGamePushProtocol;
import com.deyo.hypixel.commons.protocol.objects.game.InstantiateGamePushProtocol.Request;
import com.deyo.hypixel.commons.protocol.objects.game.InstantiateGamePushProtocol.Response;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.type.ravengarddungeon.game.DungeonInstanceRegistry;

public class DungeonInstantiateGameHandler implements RedisMessageHandler<Request, Response> {

    private static final InstantiateGamePushProtocol PROTOCOL = new InstantiateGamePushProtocol();

    @Override
    public RedisProtocol<Request, Response> protocol() {
        return PROTOCOL;
    }

    @Override
    public Response handle(Request request, RedisMessageContext context) {
        try {
            DungeonInstanceRegistry.DungeonInstance instance =
                    DungeonInstanceRegistry.create(request.gameType());
            return Response.success(instance.getGameId().toString(), "generated", instance.getMode());
        } catch (Exception exception) {
            return Response.failure("Failed to instantiate dungeon: " + exception.getMessage());
        }
    }
}
