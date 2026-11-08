package com.deyo.hypixel.commons.protocol.objects.orchestrator;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.UnderstandableProxyServer;
import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

public class GetServerForMapProtocol extends RedisProtocol
        <GetServerForMapProtocol.GetServerForMapMessage,
                GetServerForMapProtocol.GetServerForMapResponse> {
    private static final Serializer<GetServerForMapMessage> SERIALIZER =
            new JacksonSerializer<>(GetServerForMapMessage.class);
    private static final Serializer<GetServerForMapResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(GetServerForMapResponse.class);

    @Override

    public Serializer<GetServerForMapMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<GetServerForMapResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record GetServerForMapMessage(ServerType type, @Nullable String map, String mode, int neededSlots) {
    }

    public record GetServerForMapResponse(UnderstandableProxyServer server, String gameId, boolean success, @Nullable String error) {
    }
}
