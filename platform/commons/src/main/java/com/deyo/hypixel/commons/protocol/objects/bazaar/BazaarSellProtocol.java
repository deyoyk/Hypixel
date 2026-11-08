package com.deyo.hypixel.commons.protocol.objects.bazaar;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BazaarSellProtocol extends RedisProtocol<
        BazaarSellProtocol.BazaarSellMessage,
        BazaarSellProtocol.BazaarSellResponse> {
    private static final Serializer<BazaarSellMessage> SERIALIZER =
            new JacksonSerializer<>(BazaarSellMessage.class);
    private static final Serializer<BazaarSellResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(BazaarSellResponse.class);

    @Override
    public Serializer<BazaarSellMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<BazaarSellResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record BazaarSellMessage(String itemName, UUID playerUUID, UUID profileUUID, Double price, int amount) {}

    public record BazaarSellResponse(boolean success, @Nullable String error) {}
}
