package com.deyo.hypixel.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.guild.GuildResponseEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildTransferredResponseEvent extends GuildResponseEvent {
    private UUID oldOwner;
    private UUID newOwner;

    public GuildTransferredResponseEvent(GuildData guild, UUID oldOwner, UUID newOwner) {
        super(guild);
        this.oldOwner = oldOwner;
        this.newOwner = newOwner;
    }

}
