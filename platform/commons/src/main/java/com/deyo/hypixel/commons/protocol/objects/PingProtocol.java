package com.deyo.hypixel.commons.protocol.objects;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;

public class PingProtocol extends RedisProtocol<
        PingProtocol.EmptyMessage,
        PingProtocol.EmptyMessage> {
    private static final Serializer<EmptyMessage> SERIALIZER =
            new JacksonSerializer<>(EmptyMessage.class);
    private static final Serializer<EmptyMessage> RETURN_SERIALIZER =
            new JacksonSerializer<>(EmptyMessage.class);

    @Override
    public Serializer<EmptyMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<EmptyMessage> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record EmptyMessage() {}
}
