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
public final class FriendDeniedResponseEvent extends FriendResponseEvent {
    private static final Serializer<FriendDeniedResponseEvent> SERIALIZER =
            new JacksonSerializer<>(FriendDeniedResponseEvent.class);

    private final UUID denier;
    private final UUID requester;
    private final String denierName;

    @JsonCreator
    public FriendDeniedResponseEvent(@JsonProperty("denier") UUID denier, @JsonProperty("requester") UUID requester, @JsonProperty("denierName") String denierName) {
        super();
        this.denier = denier;
        this.requester = requester;
        this.denierName = denierName;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(requester);
    }

    @Override
    public Serializer<FriendDeniedResponseEvent> getSerializer() {
        return SERIALIZER;
    }
}
