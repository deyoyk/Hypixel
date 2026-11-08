package com.deyo.hypixel.type.bedwarslobby.events;

import net.minestom.server.event.player.PlayerMoveEvent;
import com.deyo.hypixel.type.bedwarslobby.gui.cosmetics.ShopkeeperPreviewController;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public class ActionShopkeeperPreviewMoveLock implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerMoveEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        if (ShopkeeperPreviewController.isLocked(player)) {
            event.setCancelled(true);
        }
    }
}
