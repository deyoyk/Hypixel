package com.deyo.hypixel.type.skyblockgeneric.museum.rewards;

import lombok.Getter;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.skyblockgeneric.levels.SkyBlockLevelCause;
import com.deyo.hypixel.type.skyblockgeneric.museum.MuseumReward;
import com.deyo.hypixel.type.skyblockgeneric.museum.MuseumRewards;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@Getter
public class MuseumXPReward extends MuseumReward {
    private final int xp;

    public MuseumXPReward(int xp) {
        this.xp = xp;
    }
    @Override
    public void onUnlock(MuseumRewards rewards, SkyBlockPlayer player) {
        player.getSkyBlockExperience().addExperience(SkyBlockLevelCause.getMuseumCause(rewards));
    }

    @Override
    public Text loreDisplay() {
        return Text.of("<b>{} SkyBlock XP", xp);
    }
}
