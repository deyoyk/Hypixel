package com.deyo.hypixel.type.skyblockgeneric.museum;

import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public abstract class MuseumReward {
    public abstract void onUnlock(MuseumRewards rewards, SkyBlockPlayer player);
    public abstract Text loreDisplay();
}
