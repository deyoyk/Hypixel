package com.deyo.hypixel.type.bedwarsgame.events;

import net.minestom.server.entity.ItemEntity;
import net.minestom.server.event.entity.EntitySpawnEvent;
import com.deyo.hypixel.type.bedwarsgame.TypeBedWarsGameLoader;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;

public class ActionItemDropped implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.ALL, requireDataLoaded = false, isAsync = false)
    public void run(EntitySpawnEvent event) {
        if (event.getEntity() instanceof ItemEntity itemEntity) {
            BedWarsGame game = TypeBedWarsGameLoader.getGameByInstance(event.getInstance());
            if (game == null) return;

            if (!game.getReplayManager().isRecording()) return;

            game.getReplayManager().recordDroppedItem(itemEntity);
        }
    }

}
