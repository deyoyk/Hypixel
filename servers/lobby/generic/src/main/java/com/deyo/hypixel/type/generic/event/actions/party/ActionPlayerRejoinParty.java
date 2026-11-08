package com.deyo.hypixel.type.generic.event.actions.party;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.proxyapi.ProxyService;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.party.PartyManager;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public class ActionPlayerRejoinParty implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true, phase = EventPhase.GAMEPLAY)
    public void run(AsyncPlayerConfigurationEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        ProxyService partyService = new ProxyService(ServiceType.PARTY);
        if (!partyService.isOnline().join()) {
            return;
        }

        // Check if player is in a party (they might have a pending disconnect timer)
        if (PartyManager.isInParty(player)) {
            // Notify the service that this player has rejoined
            // This will cancel any pending disconnect timer
            PartyManager.notifyPlayerRejoin(player.getUuid());
        }
    }
}
