package com.deyo.hypixel.type.skyblockgeneric.levels.abstr;

import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public abstract class SkyBlockLevelCauseAbstr {
    public abstract double xpReward();
    public abstract boolean shouldDisplayMessage(SkyBlockPlayer player);
}
