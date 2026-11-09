package com.deyo.hypixel.commons.protocol.objects.guild;

import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;

import java.util.UUID;

public class GetGuildProtocolObject extends RedisProtocol<GetGuildProtocolObject.GetGuildMessage, GetGuildProtocolObject.GetGuildResponse> {
    private static final Serializer<GetGuildMessage> SERIALIZER = new JacksonSerializer<>(GetGuildMessage.class);
    private static final Serializer<GetGuildResponse> RETURN_SERIALIZER = new JacksonSerializer<>(GetGuildResponse.class);

    @Override
    public Serializer<GetGuildMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<GetGuildResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record GetGuildMessage(UUID memberUUID) {
    }

    public record GetGuildResponse(GuildData guild) {
    }
}
