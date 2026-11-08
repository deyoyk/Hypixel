package com.deyo.hypixel.commons.protocol.objects.election;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

public class StartElectionProtocol
        extends RedisProtocol<StartElectionProtocol.StartElectionMessage,
        StartElectionProtocol.StartElectionResponse> {
    private static final Serializer<StartElectionMessage> SERIALIZER =
            new JacksonSerializer<>(StartElectionMessage.class);
    private static final Serializer<StartElectionResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(StartElectionResponse.class);

    @Override
    public Serializer<StartElectionMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<StartElectionResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record StartElectionMessage(int year, String candidatesJson) {}

    public record StartElectionResponse(boolean started, String serializedData, boolean success, @Nullable String error) {}
}
