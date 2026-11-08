package com.deyo.hypixel.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.guild.GuildResponseEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildMemberJoinedResponseEvent extends GuildResponseEvent {
    private UUID joiner;

    public GuildMemberJoinedResponseEvent(GuildData guild, UUID joiner) {
        super(guild);
        this.joiner = joiner;
    }

}
