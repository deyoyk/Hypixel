package com.deyo.hypixel.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.guild.GuildResponseEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildChatResponseEvent extends GuildResponseEvent {
    private UUID sender;
    private String message;
    private boolean officerChat;

    public GuildChatResponseEvent(GuildData guild, UUID sender, String message, boolean officerChat) {
        super(guild);
        this.sender = sender;
        this.message = message;
        this.officerChat = officerChat;
    }

}
