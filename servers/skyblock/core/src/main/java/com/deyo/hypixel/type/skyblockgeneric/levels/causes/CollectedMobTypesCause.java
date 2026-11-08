package com.deyo.hypixel.type.skyblockgeneric.levels.causes;

import com.deyo.hypixel.type.skyblockgeneric.levels.abstr.SkyBlockLevelCauseAbstr;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class CollectedMobTypesCause extends SkyBlockLevelCauseAbstr {
    private String mobType;

    public CollectedMobTypesCause(String mobType) {
        this.mobType = mobType;
    }

    @Override
    public double xpReward() {
        return 1;
    }

    @Override
    public boolean shouldDisplayMessage(SkyBlockPlayer player) {
        return false;
    }
}
