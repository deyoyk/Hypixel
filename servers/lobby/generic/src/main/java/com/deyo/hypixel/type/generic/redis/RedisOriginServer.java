package com.deyo.hypixel.type.generic.redis;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.proxy.from.GivePlayersOriginTypeProtocol;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class RedisOriginServer implements RedisMessageHandler<GivePlayersOriginTypeProtocol.Request, GivePlayersOriginTypeProtocol.Response> {
    public static final Map<UUID, ServerType> origin = new ConcurrentHashMap<>();

    public static ServerType consume(UUID uuid) {
        return origin.remove(uuid);
    }

    @Override
    public RedisProtocol<GivePlayersOriginTypeProtocol.Request, GivePlayersOriginTypeProtocol.Response> protocol() {
        return new GivePlayersOriginTypeProtocol();
    }

    @Override
    public GivePlayersOriginTypeProtocol.Response handle(GivePlayersOriginTypeProtocol.Request message, RedisMessageContext context) {
        UUID uuid = UUID.fromString(message.uuid());
        ServerType originType = ServerType.valueOf(message.originType());
        origin.put(uuid, originType);
        return new GivePlayersOriginTypeProtocol.Response();
    }
}