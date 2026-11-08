package com.deyo.hypixel.type.murdermysteryconfigurator.events;

import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;

public class ActionPlayerDataSpawn implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true, phase = EventPhase.POST_SPAWN)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;

        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        // Send welcome message
        player.sendMessage("<6><l>=== Murder Mystery Configurator ===");
        player.sendMessage("<e>Use <b>/choosemap \\<mapId> <e>to load a map");
        player.sendMessage("<e>Use <b>/mmsetup <e>to configure the map");
        player.sendMessage("<7>Anvil worlds will be automatically converted to Polar format");
    }
}
