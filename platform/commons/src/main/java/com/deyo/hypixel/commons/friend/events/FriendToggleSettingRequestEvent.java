package com.deyo.hypixel.commons.friend.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import com.deyo.hypixel.commons.friend.FriendEvent;
import com.deyo.hypixel.commons.friend.FriendSettingType;
import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.protocol.Serializer;

import java.util.List;
import java.util.UUID;

@Getter
public final class FriendToggleSettingRequestEvent extends FriendEvent {
    private static final Serializer<FriendToggleSettingRequestEvent> SERIALIZER =
            new JacksonSerializer<>(FriendToggleSettingRequestEvent.class);

    private final UUID player;
    private final FriendSettingType settingType;

    @JsonCreator
    public FriendToggleSettingRequestEvent(@JsonProperty("player") UUID player, @JsonProperty("settingType") FriendSettingType settingType) {
        super();
        this.player = player;
        this.settingType = settingType;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(player);
    }

    @Override
    public Serializer<FriendToggleSettingRequestEvent> getSerializer() {
        return SERIALIZER;
    }
}
