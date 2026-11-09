package com.deyo.hypixel.commons.protocol.objects.replay;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;

import java.util.UUID;

public class ReplayEndProtocolObject extends RedisProtocol<
        ReplayEndProtocolObject.EndMessage,
        ReplayEndProtocolObject.EndResponse> {
    private static final Serializer<EndMessage> SERIALIZER = new JacksonSerializer<>(EndMessage.class);
    private static final Serializer<EndResponse> RETURN_SERIALIZER = new JacksonSerializer<>(EndResponse.class);

    @Override
    public Serializer<EndMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<EndResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record EndMessage(
            UUID replayId,
            long endTime,
            int durationTicks
    ) {}

    public record EndResponse(
            boolean success,
            long totalBytes,
            long compressedBytes,
            boolean available
    ) {}
}
