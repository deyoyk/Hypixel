package com.deyo.hypixel.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.guild.GuildResponseEvent;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildMemberKickedResponseEvent extends GuildResponseEvent {
    private UUID kicker;
    private UUID kicked;
    private String reason;

    public GuildMemberKickedResponseEvent(GuildData guild, UUID kicker, UUID kicked, String reason) {
        super(guild);
        this.kicker = kicker;
        this.kicked = kicked;
        this.reason = reason;
    }

    @Override
    public List<UUID> getParticipants() {
        List<UUID> participants = new java.util.ArrayList<>(getGuild().getAllMemberUuids());
        if (!participants.contains(kicked)) participants.add(kicked);
        return participants;
    }

}
