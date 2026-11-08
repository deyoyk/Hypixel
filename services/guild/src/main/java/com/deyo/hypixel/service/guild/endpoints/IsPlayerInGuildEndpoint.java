package com.deyo.hypixel.service.guild.endpoints;

import com.deyo.hypixel.commons.protocol.objects.guild.IsPlayerInGuildProtocolObject;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.commons.redis.RedisMessageContext;
import com.deyo.hypixel.service.guild.GuildCache;

public class IsPlayerInGuildEndpoint implements RedisMessageHandler<
    IsPlayerInGuildProtocolObject.IsPlayerInGuildMessage,
    IsPlayerInGuildProtocolObject.IsPlayerInGuildResponse> {

    @Override
    public IsPlayerInGuildProtocolObject protocol() {
        return new IsPlayerInGuildProtocolObject();
    }

    @Override
    public IsPlayerInGuildProtocolObject.IsPlayerInGuildResponse handle(IsPlayerInGuildProtocolObject.IsPlayerInGuildMessage messageObject, RedisMessageContext context) {
        return new IsPlayerInGuildProtocolObject.IsPlayerInGuildResponse(
            GuildCache.isInGuild(messageObject.playerUUID())
        );
    }
}
