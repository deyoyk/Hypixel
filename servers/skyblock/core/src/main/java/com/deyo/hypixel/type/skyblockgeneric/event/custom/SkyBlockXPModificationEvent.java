package com.deyo.hypixel.type.skyblockgeneric.event.custom;

import lombok.Getter;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import com.deyo.hypixel.type.skyblockgeneric.levels.abstr.SkyBlockLevelCauseAbstr;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@Getter
public class SkyBlockXPModificationEvent implements PlayerInstanceEvent {
    @Getter
    private SkyBlockPlayer player;
    private SkyBlockLevelCauseAbstr cause;
    private double oldXP;
    private double newXP;

    public SkyBlockXPModificationEvent(SkyBlockPlayer player, SkyBlockLevelCauseAbstr cause, double oldXP, double newXP) {
        this.player = player;
        this.cause = cause;
        this.oldXP = oldXP;
        this.newXP = newXP;
    }
}
