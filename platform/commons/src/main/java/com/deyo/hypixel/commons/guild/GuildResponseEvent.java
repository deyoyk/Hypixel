package com.deyo.hypixel.commons.guild;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class GuildResponseEvent extends GuildEvent {
    public GuildResponseEvent(GuildData guild) {
        super(guild);
    }
}
