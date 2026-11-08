package com.deyo.hypixel.type.skyblockgeneric.item.handlers.place;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@FunctionalInterface
public interface PlaceEventHandler {
    void onPlace(PlayerBlockPlaceEvent event, SkyBlockPlayer player, SkyBlockItem item);
}