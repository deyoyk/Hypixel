package com.deyo.hypixel.commons.protocol.objects.friend;

import com.deyo.hypixel.commons.friend.FriendData;
import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;

import java.util.UUID;
import org.jetbrains.annotations.Nullable;

public class GetFriendDataProtocol extends RedisProtocol
        <GetFriendDataProtocol.GetFriendDataMessage,
                GetFriendDataProtocol.GetFriendDataResponse> {
    private static final Serializer<GetFriendDataMessage> SERIALIZER =
            new JacksonSerializer<>(GetFriendDataMessage.class);
    private static final Serializer<GetFriendDataResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(GetFriendDataResponse.class);

    @Override

    public Serializer<GetFriendDataMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<GetFriendDataResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record GetFriendDataMessage(UUID playerUuid) {
    }

    public record GetFriendDataResponse(FriendData friendData, boolean success, @Nullable String error) {
    }
}
