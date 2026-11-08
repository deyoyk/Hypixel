package com.deyo.hypixel.type.island.events.traditional;

import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventHandler;
import com.deyo.hypixel.type.island.gui.GUIJerry;
import com.deyo.hypixel.type.skyblockgeneric.event.custom.JerryClickedEvent;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerClickedJerryNPC implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerEntityInteractEvent event) {
        final SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getHand() != PlayerHand.MAIN) return;
        if (event.getTarget().getEntityType() != EntityType.VILLAGER) return;

        JerryClickedEvent jerryClickedEvent = new JerryClickedEvent(player);
        HypixelEventHandler.callCustomEvent(jerryClickedEvent);

        if (jerryClickedEvent.isCancelled()) return;

        new GUIJerry().open(player);
    }
}
