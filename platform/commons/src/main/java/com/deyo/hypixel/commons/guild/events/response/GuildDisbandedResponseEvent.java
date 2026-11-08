package com.deyo.hypixel.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.guild.GuildResponseEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildDisbandedResponseEvent extends GuildResponseEvent {
    private UUID disbander;

    public GuildDisbandedResponseEvent(GuildData guild, UUID disbander) {
        super(guild);
        this.disbander = disbander;
    }

}
