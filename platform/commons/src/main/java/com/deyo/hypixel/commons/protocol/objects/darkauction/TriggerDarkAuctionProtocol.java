package com.deyo.hypixel.commons.protocol.objects.darkauction;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

public class TriggerDarkAuctionProtocol extends RedisProtocol<
        TriggerDarkAuctionProtocol.TriggerMessage,
        TriggerDarkAuctionProtocol.TriggerResponse> {

    private static final Serializer<TriggerMessage> MESSAGE_SERIALIZER =
            new JacksonSerializer<>(TriggerMessage.class);
    private static final Serializer<TriggerResponse> RESPONSE_SERIALIZER =
            new JacksonSerializer<>(TriggerResponse.class);

    @Override
    public Serializer<TriggerMessage> getSerializer() {
        return MESSAGE_SERIALIZER;
    }

    @Override
    public Serializer<TriggerResponse> getReturnSerializer() {
        return RESPONSE_SERIALIZER;
    }

    public record TriggerMessage(long calendarTime, boolean forced) {
        public TriggerMessage(long calendarTime) {
            this(calendarTime, false);
        }
    }

    public record TriggerResponse(boolean success, String message, @Nullable String error) {}
}
