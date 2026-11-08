package com.deyo.hypixel.type.skyblockgeneric.event.actions.player;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.proxyapi.ProxyPlayer;
import com.deyo.hypixel.proxyapi.ProxyService;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.party.PartyManager;
import com.deyo.hypixel.type.generic.utility.ScheduleUtility;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerTransferServerParty implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerDisconnectEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        // Wait 60 ticks (3 seconds) to give the player time to connect to a new server
        ScheduleUtility.delay(() -> {
            ProxyService partyService = new ProxyService(ServiceType.PARTY);
            if (!partyService.isOnline().join()) {
                return;
            }

            // Check if the player is still online on the network
            ProxyPlayer proxyPlayer = new ProxyPlayer(player.getUuid());
            boolean isOnline = proxyPlayer.isOnline().join();

            if (isOnline) {
                // Player is on another server - just a server transfer
                PartyManager.switchPartyServer(player);
            } else {
                // Player is truly offline - check if they're in a party and notify
                if (PartyManager.isInParty(player)) {
                    PartyManager.notifyPlayerDisconnect(player.getUuid());
                }
            }
        }, 60);
    }
}
