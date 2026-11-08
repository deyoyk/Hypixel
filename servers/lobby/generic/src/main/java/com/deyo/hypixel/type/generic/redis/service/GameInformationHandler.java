package com.deyo.hypixel.type.generic.redis.service;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.game.GameInformationPushProtocol;
import com.deyo.hypixel.commons.protocol.objects.game.GameInformationPushProtocol.Request;
import com.deyo.hypixel.commons.protocol.objects.game.GameInformationPushProtocol.Response;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class GameInformationHandler implements RedisMessageHandler<Request, Response> {

    public static Map<UUID, String> game = new HashMap<>();

    private static final GameInformationPushProtocol PROTOCOL = new GameInformationPushProtocol();

    @Override
    public RedisProtocol<Request, Response> protocol() {
        return PROTOCOL;
    }

    @Override
    public Response handle(Request message, RedisMessageContext context) {
        game.put(message.uuid(), message.gameId());
        return new Response();
    }
}
