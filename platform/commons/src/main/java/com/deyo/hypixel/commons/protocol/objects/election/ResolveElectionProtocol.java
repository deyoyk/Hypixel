package com.deyo.hypixel.commons.protocol.objects.election;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

public class ResolveElectionProtocol
        extends RedisProtocol<ResolveElectionProtocol.ResolveElectionMessage,
        ResolveElectionProtocol.ResolveElectionResponse> {
    private static final Serializer<ResolveElectionMessage> SERIALIZER =
            new JacksonSerializer<>(ResolveElectionMessage.class);
    private static final Serializer<ResolveElectionResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(ResolveElectionResponse.class);

    @Override
    public Serializer<ResolveElectionMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<ResolveElectionResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record ResolveElectionMessage(int year) {}

    public record ResolveElectionResponse(boolean resolved, String serializedData, boolean success, @Nullable String error) {}
}
