package com.deyo.hypixel.type.skyblockgeneric.redis;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.proxy.from.DoesServerHaveIslandProtocol;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.type.skyblockgeneric.user.island.SkyBlockIsland;

import java.util.UUID;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class RedisHasIslandLoaded implements RedisMessageHandler<DoesServerHaveIslandProtocol.Request, DoesServerHaveIslandProtocol.Response> {
    @Override
    public RedisProtocol<DoesServerHaveIslandProtocol.Request, DoesServerHaveIslandProtocol.Response> protocol() {
        return new DoesServerHaveIslandProtocol();
    }

    @Override
    public DoesServerHaveIslandProtocol.Response handle(DoesServerHaveIslandProtocol.Request message, RedisMessageContext context) {
        UUID islandUUID = UUID.fromString(message.islandUuid());
        return new DoesServerHaveIslandProtocol.Response(SkyBlockIsland.hasIsland(islandUUID), true, null);
    }
}
