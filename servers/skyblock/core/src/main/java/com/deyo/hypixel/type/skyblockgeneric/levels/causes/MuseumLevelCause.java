package com.deyo.hypixel.type.skyblockgeneric.levels.causes;

import lombok.Getter;
import com.deyo.hypixel.type.skyblockgeneric.levels.abstr.SkyBlockLevelCauseAbstr;
import com.deyo.hypixel.type.skyblockgeneric.museum.MuseumRewards;
import com.deyo.hypixel.type.skyblockgeneric.museum.rewards.MuseumXPReward;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@Getter
public class MuseumLevelCause extends SkyBlockLevelCauseAbstr {
    private MuseumRewards reward;

    public MuseumLevelCause(MuseumRewards reward) {
        this.reward = reward;
    }

    @Override
    public double xpReward() {
        MuseumXPReward xpReward = reward.getXPReward();

        if (xpReward != null) {
            return xpReward.getXp();
        }

        return 0;
    }

    @Override
    public boolean shouldDisplayMessage(SkyBlockPlayer player) {
        return false;
    }
}
