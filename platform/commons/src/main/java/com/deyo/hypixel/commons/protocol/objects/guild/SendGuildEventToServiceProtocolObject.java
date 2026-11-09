package com.deyo.hypixel.commons.protocol.objects.guild;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;

public class SendGuildEventToServiceProtocolObject extends RedisProtocol<SendGuildEventToServiceProtocolObject.SendGuildEventToServiceMessage, SendGuildEventToServiceProtocolObject.SendGuildEventToServiceResponse> {
    private static final Serializer<SendGuildEventToServiceMessage> SERIALIZER = new JacksonSerializer<>(SendGuildEventToServiceMessage.class);
    private static final Serializer<SendGuildEventToServiceResponse> RETURN_SERIALIZER = new JacksonSerializer<>(SendGuildEventToServiceResponse.class);

    @Override
    public Serializer<SendGuildEventToServiceMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<SendGuildEventToServiceResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record SendGuildEventToServiceMessage(String eventType, String eventData) {
    }

    public record SendGuildEventToServiceResponse(boolean success) {
    }
}
