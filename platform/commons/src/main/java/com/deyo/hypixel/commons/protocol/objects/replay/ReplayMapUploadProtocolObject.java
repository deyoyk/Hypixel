package com.deyo.hypixel.commons.protocol.objects.replay;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;

public class ReplayMapUploadProtocolObject extends RedisProtocol<
        ReplayMapUploadProtocolObject.MapUploadMessage,
        ReplayMapUploadProtocolObject.MapUploadResponse> {
    private static final Serializer<MapUploadMessage> SERIALIZER = new JacksonSerializer<>(MapUploadMessage.class);
    private static final Serializer<MapUploadResponse> RETURN_SERIALIZER = new JacksonSerializer<>(MapUploadResponse.class);

    @Override
    public Serializer<MapUploadMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<MapUploadResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record MapUploadMessage(
            String mapHash,
            String mapName,
            byte[] compressedData
    ) {}

    public record MapUploadResponse(boolean success, boolean alreadyExists) {}
}
