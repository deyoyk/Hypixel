package com.deyo.hypixel.commons.protocol.objects.friend;

import com.deyo.hypixel.commons.friend.FriendEvent;
import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;
import org.jetbrains.annotations.Nullable;

public class SendFriendEventToServiceProtocol extends RedisProtocol
        <SendFriendEventToServiceProtocol.SendFriendEventToServiceMessage,
                SendFriendEventToServiceProtocol.SendFriendEventToServiceResponse> {
    private static final Serializer<SendFriendEventToServiceMessage> SERIALIZER =
            new JacksonSerializer<>(SendFriendEventToServiceMessage.class);
    private static final Serializer<SendFriendEventToServiceResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(SendFriendEventToServiceResponse.class);

    @Override

    public Serializer<SendFriendEventToServiceMessage> getSerializer() {

        return SERIALIZER;

    }

    @Override

    public Serializer<SendFriendEventToServiceResponse> getReturnSerializer() {

        return RETURN_SERIALIZER;

    }

    public record SendFriendEventToServiceMessage(FriendEvent event) {
    }

    public record SendFriendEventToServiceResponse(boolean success, @Nullable String error) {
    }
}
