package com.deyo.hypixel.commons.protocol.objects.party;

import com.deyo.hypixel.commons.party.Party;
import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class GetPartyProtocol extends RedisProtocol
        <GetPartyProtocol.GetPartyMessage,
                GetPartyProtocol.GetPartyResponse> {
    private static final Serializer<GetPartyMessage> SERIALIZER =
            new JacksonSerializer<>(GetPartyMessage.class);
    private static final Serializer<GetPartyResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(GetPartyResponse.class);

    @Override

    public Serializer<GetPartyMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<GetPartyResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record GetPartyMessage(UUID memberUuid) { }

    public record GetPartyResponse(Party party, boolean success, @Nullable String error) { }
}
