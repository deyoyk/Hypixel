package com.deyo.hypixel.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.guild.GuildResponseEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildRankChangedResponseEvent extends GuildResponseEvent {
    private UUID changer;
    private UUID target;
    private String fromRank;
    private String toRank;

    public GuildRankChangedResponseEvent(GuildData guild, UUID changer, UUID target, String fromRank, String toRank) {
        super(guild);
        this.changer = changer;
        this.target = target;
        this.fromRank = fromRank;
        this.toRank = toRank;
    }

}
