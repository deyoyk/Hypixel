package com.deyo.hypixel.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.guild.GuildResponseEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildCreatedResponseEvent extends GuildResponseEvent {
    private UUID creator;

    public GuildCreatedResponseEvent(GuildData guild, UUID creator) {
        super(guild);
        this.creator = creator;
    }

}
