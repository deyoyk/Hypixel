package com.deyo.hypixel.type.skyblockgeneric.event.actions.player;

import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionClearPendingBazaar implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerSpawnEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        player.getBazaarConnector().processAllPendingTransactions();
    }
}
