package com.deyo.hypixel.type.hub.events;

import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.hub.gui.GUIRunicPedestal;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerClicksOnRunePedestal implements HypixelEventClass {


    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerBlockInteractEvent event) {
        if (!event.getBlockPosition().equals(new BlockVec(23, 64, -135))) return;

        new GUIRunicPedestal().open((SkyBlockPlayer) event.getPlayer());
    }
}
