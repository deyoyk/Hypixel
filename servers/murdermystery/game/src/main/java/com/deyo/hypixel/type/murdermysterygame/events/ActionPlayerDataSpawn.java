package com.deyo.hypixel.type.murdermysterygame.events;

import net.minestom.server.entity.EntityType;
import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.pvp.projectile.BowModule;
import com.deyo.hypixel.pvp.projectile.entities.ArrowProjectile;
import com.deyo.hypixel.type.murdermysterygame.user.MurderMysteryPlayer;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class ActionPlayerDataSpawn implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, isAsync = true, phase = EventPhase.POST_SPAWN)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;
        final MurderMysteryPlayer player = (MurderMysteryPlayer) event.getPlayer();

        // Registers the bow module for this player
        new BowModule(player.eventNode(), (p, i) -> new ArrowProjectile(EntityType.ARROW, p));
    }
}
