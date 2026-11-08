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
public final class FriendJoinNotificationEvent extends FriendResponseEvent {
    private static final Serializer<FriendJoinNotificationEvent> SERIALIZER =
            new JacksonSerializer<>(FriendJoinNotificationEvent.class);

    private final UUID player;
    private final UUID friend;
    private final String friendName;

    @JsonCreator
    public FriendJoinNotificationEvent(@JsonProperty("player") UUID player, @JsonProperty("friend") UUID friend, @JsonProperty("friendName") String friendName) {
        super();
        this.player = player;
        this.friend = friend;
        this.friendName = friendName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendJoinNotificationEvent> getSerializer() {
        return SERIALIZER;
    }
}
