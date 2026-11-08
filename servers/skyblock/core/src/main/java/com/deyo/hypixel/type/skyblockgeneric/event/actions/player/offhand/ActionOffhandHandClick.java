package com.deyo.hypixel.type.skyblockgeneric.event.actions.player.offhand;

import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionOffhandHandClick implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.GAMEPLAY)
    public void run(PlayerHandAnimationEvent event) {
        if (event.getHand().equals(PlayerHand.OFF)) {
            ((SkyBlockPlayer) event.getPlayer()).sendMessage("<c>You cannot use your offhand!");
            event.setCancelled(true);
        }
    }
}
