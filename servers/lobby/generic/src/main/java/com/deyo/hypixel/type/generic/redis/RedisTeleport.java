package com.deyo.hypixel.type.generic.redis;

import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.proxy.from.TeleportProtocol;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.type.generic.HypixelGenericLoader;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

import java.util.UUID;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class RedisTeleport implements RedisMessageHandler<TeleportProtocol.Request, TeleportProtocol.Response> {
    @Override
    public RedisProtocol<TeleportProtocol.Request, TeleportProtocol.Response> protocol() {
        return new TeleportProtocol();
    }

    @Override
    public TeleportProtocol.Response handle(TeleportProtocol.Request message, RedisMessageContext context) {
        UUID uuid = UUID.fromString(message.uuid());
        HypixelPlayer player = HypixelGenericLoader.getFromUUID(uuid);
        if (player == null) return new TeleportProtocol.Response();
        player.teleport(new Pos(message.x(), message.y(), message.z(), message.yaw(), message.pitch()));
        return new TeleportProtocol.Response();
    }
}
