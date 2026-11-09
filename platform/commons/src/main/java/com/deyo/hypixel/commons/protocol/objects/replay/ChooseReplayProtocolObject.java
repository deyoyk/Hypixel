package com.deyo.hypixel.commons.protocol.objects.replay;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ChooseReplayProtocolObject extends RedisProtocol
        <ChooseReplayProtocolObject.ChooseReplayMessage,
            ChooseReplayProtocolObject.ChooseReplayResponse> {
    private static final Serializer<ChooseReplayMessage> SERIALIZER = new JacksonSerializer<>(ChooseReplayMessage.class);
    private static final Serializer<ChooseReplayResponse> RETURN_SERIALIZER = new JacksonSerializer<>(ChooseReplayResponse.class);

    @Override
    public Serializer<ChooseReplayMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<ChooseReplayResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record ChooseReplayMessage(UUID player, String replayId, @Nullable String shareCode) {
        public ChooseReplayMessage(UUID player, String replayId) {
            this(player, replayId, null);
        }
    }

    public record ChooseReplayResponse(boolean error) {
    }
}
