package com.deyo.hypixel.type.skyblockgeneric.levels.causes;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.skyblockgeneric.levels.abstr.SkyBlockLevelCauseAbstr;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class NewAccessoryLevelCause extends SkyBlockLevelCauseAbstr {
    public ItemType itemTypeLinker;

    public NewAccessoryLevelCause(ItemType itemTypeLinker) {
        this.itemTypeLinker = itemTypeLinker;
    }

    @Override
    public double xpReward() {
        return 1;
    }

    @Override
    public boolean shouldDisplayMessage(SkyBlockPlayer player) {
        return true;
    }
}
