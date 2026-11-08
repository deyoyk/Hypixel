package com.deyo.hypixel.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.guild.GuildResponseEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildMuteChangedResponseEvent extends GuildResponseEvent {
    private UUID muter;
    private String target;
    private long duration;
    private boolean unmute;

    public GuildMuteChangedResponseEvent(GuildData guild, UUID muter, String target, long duration, boolean unmute) {
        super(guild);
        this.muter = muter;
        this.target = target;
        this.duration = duration;
        this.unmute = unmute;
    }

}
