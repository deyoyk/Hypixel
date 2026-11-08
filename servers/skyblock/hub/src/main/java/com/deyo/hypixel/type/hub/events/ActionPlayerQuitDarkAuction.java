package com.deyo.hypixel.type.hub.events;

import net.minestom.server.event.player.PlayerDisconnectEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.darkauction.DarkAuctionHandler;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

/**
 * Handles cleanup when a player disconnects while in the Dark Auction.
 * This ensures the service is notified and local caches are cleared.
 */
public class ActionPlayerQuitDarkAuction implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerDisconnectEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        // Let the handler deal with cleanup and service notification
        DarkAuctionHandler.handlePlayerLeft(player);
    }
}
