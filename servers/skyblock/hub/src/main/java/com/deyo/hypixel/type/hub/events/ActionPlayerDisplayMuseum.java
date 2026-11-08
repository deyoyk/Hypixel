package com.deyo.hypixel.type.hub.events;

import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.museum.MuseumDisplays;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ActionPlayerDisplayMuseum implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, isAsync = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerSpawnEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();
        if (!event.isFirstSpawn()) return;

        CompletableFuture.delayedExecutor(2000, TimeUnit.MILLISECONDS)
                .execute(() -> {
                    if (!player.isOnline()) return;
                    MuseumDisplays.updateDisplay(player);
                });
    }
}
