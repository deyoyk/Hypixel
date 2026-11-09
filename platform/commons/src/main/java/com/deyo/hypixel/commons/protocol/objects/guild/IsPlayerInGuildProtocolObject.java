package com.deyo.hypixel.commons.protocol.objects.guild;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;

import java.util.UUID;

public class IsPlayerInGuildProtocolObject extends RedisProtocol<IsPlayerInGuildProtocolObject.IsPlayerInGuildMessage, IsPlayerInGuildProtocolObject.IsPlayerInGuildResponse> {
    private static final Serializer<IsPlayerInGuildMessage> SERIALIZER = new JacksonSerializer<>(IsPlayerInGuildMessage.class);
    private static final Serializer<IsPlayerInGuildResponse> RETURN_SERIALIZER = new JacksonSerializer<>(IsPlayerInGuildResponse.class);

    @Override
    public Serializer<IsPlayerInGuildMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<IsPlayerInGuildResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record IsPlayerInGuildMessage(UUID playerUUID) {
    }

    public record IsPlayerInGuildResponse(boolean isInGuild) {
    }
}
