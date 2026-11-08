package com.deyo.hypixel.commons.guild.events.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.deyo.hypixel.commons.guild.GuildData;
import com.deyo.hypixel.commons.guild.GuildResponseEvent;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class GuildSettingChangedResponseEvent extends GuildResponseEvent {
    private UUID changer;
    private String setting;
    private String value;

    public GuildSettingChangedResponseEvent(GuildData guild, UUID changer, String setting, String value) {
        super(guild);
        this.changer = changer;
        this.setting = setting;
        this.value = value;
    }

}
