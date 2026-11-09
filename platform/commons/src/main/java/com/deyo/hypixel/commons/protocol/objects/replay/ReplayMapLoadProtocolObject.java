package com.deyo.hypixel.commons.protocol.objects.replay;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;

public class ReplayMapLoadProtocolObject extends RedisProtocol<
    ReplayMapLoadProtocolObject.MapLoadRequest,
    ReplayMapLoadProtocolObject.MapLoadResponse> {
    private static final Serializer<MapLoadRequest> SERIALIZER = new JacksonSerializer<>(MapLoadRequest.class);
    private static final Serializer<MapLoadResponse> RETURN_SERIALIZER = new JacksonSerializer<>(MapLoadResponse.class);

    @Override
    public Serializer<MapLoadRequest> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<MapLoadResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record MapLoadRequest(String mapHash) {
    }

    public record MapLoadResponse(
        boolean success,
        boolean found,
        String mapHash,
        byte[] compressedData,
        int uncompressedSize
    ) {
    }
}
