package com.deyo.hypixel.commons.protocol.objects.guild;

import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;

import java.util.UUID;

public class GetGuildProtocolObject extends RedisProtocol<GetGuildProtocolObject.GetGuildMessage, GetGuildProtocolObject.GetGuildResponse> {
    @Override
    public Serializer<GetGuildMessage> getSerializer() {
        return new JacksonSerializer<>(GetGuildMessage.class);
    }

    @Override
    public Serializer<GetGuildResponse> getReturnSerializer() {
        return new JacksonSerializer<>(GetGuildResponse.class);
    }

    public record GetGuildMessage(UUID memberUUID) {
    }

    public record GetGuildResponse(GuildData guild) {
    }
}
