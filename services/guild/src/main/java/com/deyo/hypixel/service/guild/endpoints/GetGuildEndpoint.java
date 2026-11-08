package com.deyo.hypixel.service.guild.endpoints;

import com.deyo.hypixel.commons.protocol.objects.guild.GetGuildProtocolObject;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.service.guild.GuildCache;

public class GetGuildEndpoint implements RedisMessageHandler<
    GetGuildProtocolObject.GetGuildMessage,
    GetGuildProtocolObject.GetGuildResponse> {

    @Override
    public GetGuildProtocolObject protocol() {
        return new GetGuildProtocolObject();
    }

    @Override
    public GetGuildProtocolObject.GetGuildResponse handle(GetGuildProtocolObject.GetGuildMessage messageObject, RedisMessageContext context) {
        return new GetGuildProtocolObject.GetGuildResponse(
            GuildCache.getGuildFromPlayer(messageObject.memberUUID())
        );
    }
}
