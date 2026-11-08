package com.deyo.hypixel.commons.protocol.objects.guild;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;

import java.util.UUID;

public class IsPlayerInGuildProtocolObject extends RedisProtocol<IsPlayerInGuildProtocolObject.IsPlayerInGuildMessage, IsPlayerInGuildProtocolObject.IsPlayerInGuildResponse> {
    @Override
    public Serializer<IsPlayerInGuildMessage> getSerializer() {
        return new JacksonSerializer<>(IsPlayerInGuildMessage.class);
    }

    @Override
    public Serializer<IsPlayerInGuildResponse> getReturnSerializer() {
        return new JacksonSerializer<>(IsPlayerInGuildResponse.class);
    }

    public record IsPlayerInGuildMessage(UUID playerUUID) {
    }

    public record IsPlayerInGuildResponse(boolean isInGuild) {
    }
}
