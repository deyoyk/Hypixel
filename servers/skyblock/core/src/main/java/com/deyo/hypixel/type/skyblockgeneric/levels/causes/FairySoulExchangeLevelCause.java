package com.deyo.hypixel.type.skyblockgeneric.levels.causes;

import lombok.Getter;
import com.deyo.hypixel.type.skyblockgeneric.levels.abstr.SkyBlockLevelCauseAbstr;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.skyblockgeneric.user.fairysouls.FairySoulExchangeLevels;

@Getter
public class FairySoulExchangeLevelCause extends SkyBlockLevelCauseAbstr {
    private final FairySoulExchangeLevels level;
    private int exchangeCount;

    public FairySoulExchangeLevelCause(int exchangeCount) {
        this.exchangeCount = exchangeCount;
        this.level = FairySoulExchangeLevels.getLevel(exchangeCount);
    }

    @Override
    public double xpReward() {
        return 0;
    }

    @Override
    public boolean shouldDisplayMessage(SkyBlockPlayer player) {
        return false;
    }
}
