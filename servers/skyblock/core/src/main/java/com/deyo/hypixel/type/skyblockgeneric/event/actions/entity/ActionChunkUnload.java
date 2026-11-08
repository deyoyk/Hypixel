package com.deyo.hypixel.type.skyblockgeneric.event.actions.entity;

import net.minestom.server.event.instance.InstanceChunkUnloadEvent;
import net.minestom.server.instance.Instance;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class ActionChunkUnload implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.ENTITY, requireDataLoaded = false, isAsync = true, phase = EventPhase.GAMEPLAY)
    public void run(InstanceChunkUnloadEvent event) {
        Instance instance = event.getInstance();
        int chunkX = event.getChunkX();
        int chunkZ = event.getChunkZ();

        if (HypixelConst.isIslandServer()) return;

        instance.loadChunk(chunkX, chunkZ).join();
    }
}
