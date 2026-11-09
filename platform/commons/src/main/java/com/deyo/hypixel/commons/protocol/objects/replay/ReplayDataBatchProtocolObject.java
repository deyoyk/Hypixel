package com.deyo.hypixel.commons.protocol.objects.replay;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;

import java.util.UUID;

public class ReplayDataBatchProtocolObject extends RedisProtocol<
        ReplayDataBatchProtocolObject.BatchMessage,
        ReplayDataBatchProtocolObject.BatchResponse> {
    private static final Serializer<BatchMessage> SERIALIZER = new JacksonSerializer<>(BatchMessage.class);
    private static final Serializer<BatchResponse> RETURN_SERIALIZER = new JacksonSerializer<>(BatchResponse.class);

    @Override
    public Serializer<BatchMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<BatchResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record BatchMessage(
            UUID replayId,
            int batchIndex,
            int startTick,
            int endTick,
            int recordableCount,
            byte[] compressedData
    ) {}

    public record BatchResponse(boolean success, long bytesReceived) {}
}
