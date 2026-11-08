package com.deyo.hypixel.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.guild.GuildResponseEvent;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildInviteExpiredResponseEvent extends GuildResponseEvent {
    private UUID inviter;
    private UUID invitee;

    public GuildInviteExpiredResponseEvent(GuildData guild, UUID inviter, UUID invitee) {
        super(guild);
        this.inviter = inviter;
        this.invitee = invitee;
    }

    @Override
    public List<UUID> getParticipants() {
        return List.of(inviter, invitee);
    }

}
