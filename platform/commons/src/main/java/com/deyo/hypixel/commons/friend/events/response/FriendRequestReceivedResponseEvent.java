package com.deyo.hypixel.commons.friend.events.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import com.deyo.hypixel.commons.friend.FriendResponseEvent;
import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;

@Getter
public final class FriendRequestReceivedResponseEvent extends FriendResponseEvent {
    private static final Serializer<FriendRequestReceivedResponseEvent> SERIALIZER =
            new JacksonSerializer<>(FriendRequestReceivedResponseEvent.class);

    private final UUID sender;
    private final UUID target;
    private final String senderName;

    @JsonCreator
    public FriendRequestReceivedResponseEvent(@JsonProperty("sender") UUID sender, @JsonProperty("target") UUID target, @JsonProperty("senderName") String senderName) {
        super();
        this.sender = sender;
        this.target = target;
        this.senderName = senderName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(target);
    }

    @Override
    public Serializer<FriendRequestReceivedResponseEvent> getSerializer() {
        return SERIALIZER;
    }
}
