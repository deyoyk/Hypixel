package com.deyo.hypixel.commons.protocol.objects.darkauction;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerLeftAuctionProtocol extends RedisProtocol<
        PlayerLeftAuctionProtocol.PlayerLeftMessage,
        PlayerLeftAuctionProtocol.PlayerLeftResponse> {

    private static final Serializer<PlayerLeftMessage> MESSAGE_SERIALIZER =
            new JacksonSerializer<>(PlayerLeftMessage.class);
    private static final Serializer<PlayerLeftResponse> RESPONSE_SERIALIZER =
            new JacksonSerializer<>(PlayerLeftResponse.class);

    @Override
    public Serializer<PlayerLeftMessage> getSerializer() {
        return MESSAGE_SERIALIZER;
    }

    @Override
    public Serializer<PlayerLeftResponse> getReturnSerializer() {
        return RESPONSE_SERIALIZER;
    }

    public record PlayerLeftMessage(
            UUID playerId,
            String playerName,
            UUID auctionId
    ) {}

    public record PlayerLeftResponse(
            boolean success,
            long refundAmount,
            @Nullable String error
    ) {}
}
