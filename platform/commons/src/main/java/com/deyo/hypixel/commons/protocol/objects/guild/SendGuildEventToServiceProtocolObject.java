package com.deyo.hypixel.commons.protocol.objects.guild;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;

public class SendGuildEventToServiceProtocolObject extends RedisProtocol<SendGuildEventToServiceProtocolObject.SendGuildEventToServiceMessage, SendGuildEventToServiceProtocolObject.SendGuildEventToServiceResponse> {
    @Override
    public Serializer<SendGuildEventToServiceMessage> getSerializer() {
        return new JacksonSerializer<>(SendGuildEventToServiceMessage.class);
    }

    @Override
    public Serializer<SendGuildEventToServiceResponse> getReturnSerializer() {
        return new JacksonSerializer<>(SendGuildEventToServiceResponse.class);
    }

    public record SendGuildEventToServiceMessage(String eventType, String eventData) {
    }

    public record SendGuildEventToServiceResponse(boolean success) {
    }
}
