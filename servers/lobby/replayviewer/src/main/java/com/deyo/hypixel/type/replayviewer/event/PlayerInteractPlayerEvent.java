package com.deyo.hypixel.type.replayviewer.event;

import net.minestom.server.entity.Entity;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.replayviewer.entity.ReplayPlayerEntity;
import com.deyo.hypixel.type.replayviewer.view.GUIViewPlayer;

public class PlayerInteractPlayerEvent implements HypixelEventClass {

    // this feels pretty cool
    @PhasedEvent(node = EventNodes.ALL, requireDataLoaded = false)
    public void run(PlayerEntityInteractEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        final Entity target = event.getTarget();

        if (!(target instanceof ReplayPlayerEntity targetPlayer)) return;

        player.openView(new GUIViewPlayer(), new GUIViewPlayer.State(targetPlayer));
    }

}
