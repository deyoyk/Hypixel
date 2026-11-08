package com.deyo.hypixel.type.skyblockgeneric.item.handlers.bow;

import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@FunctionalInterface
public interface BowHandler {
    void onShoot(SkyBlockPlayer player, SkyBlockItem item, double power);
}
