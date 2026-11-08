package com.deyo.hypixel.type.skyblockgeneric.event.actions.player;

import net.minestom.server.event.player.PlayerEntityInteractEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.entity.EntityFairySoul;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.skyblockgeneric.user.fairysouls.FairySoul;

public class ActionPlayerInteractFairySoul implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerEntityInteractEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getTarget() instanceof EntityFairySoul entitySoul) {
            FairySoul fairySoul = entitySoul.parent;
            if (fairySoul == null) return;

            fairySoul.collect(player);
        }
    }
}
