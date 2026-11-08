package com.deyo.hypixel.commons.protocol.objects.darkauction;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlaceBidProtocol extends RedisProtocol<
        PlaceBidProtocol.PlaceBidMessage,
        PlaceBidProtocol.PlaceBidResponse> {

    private static final Serializer<PlaceBidMessage> MESSAGE_SERIALIZER =
            new JacksonSerializer<>(PlaceBidMessage.class);
    private static final Serializer<PlaceBidResponse> RESPONSE_SERIALIZER =
            new JacksonSerializer<>(PlaceBidResponse.class);

    @Override
    public Serializer<PlaceBidMessage> getSerializer() {
        return MESSAGE_SERIALIZER;
    }

    @Override
    public Serializer<PlaceBidResponse> getReturnSerializer() {
        return RESPONSE_SERIALIZER;
    }

    public record PlaceBidMessage(
            UUID auctionId,
            UUID playerId,
            String playerName,
            long bidAmount
    ) {}

    public record PlaceBidResponse(
            boolean success,
            String message,
            @Nullable String error
    ) {}
}
