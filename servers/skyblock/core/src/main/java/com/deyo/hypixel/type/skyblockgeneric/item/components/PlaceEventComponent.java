package com.deyo.hypixel.type.skyblockgeneric.item.components;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItemComponent;
import com.deyo.hypixel.type.skyblockgeneric.item.handlers.place.PlaceEventHandler;
import com.deyo.hypixel.type.skyblockgeneric.item.handlers.place.PlaceEventRegistry;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class PlaceEventComponent extends SkyBlockItemComponent {
    private final String handlerId;

    public PlaceEventComponent(String handlerId) {
        this.handlerId = handlerId;
    }

    public void handlePlace(PlayerBlockPlaceEvent event, SkyBlockPlayer player, SkyBlockItem item) {
        PlaceEventHandler handler = PlaceEventRegistry.getHandler(handlerId);
        if (handler != null) {
            handler.onPlace(event, player, item);
        }
    }
}
