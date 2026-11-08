package com.deyo.hypixel.type.island.events.traditional;

import net.minestom.server.event.player.PlayerEntityInteractEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.skyblockgeneric.entity.MinionEntityImpl;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.GUIMinion;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

public class ActionPlayerClickMinion implements HypixelEventClass {


    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = true, phase = EventPhase.GAMEPLAY)
    public void run(PlayerEntityInteractEvent event) {
        SkyBlockPlayer player = (SkyBlockPlayer) event.getPlayer();

        if (event.getTarget() instanceof MinionEntityImpl minion) {
            new GUIMinion(minion.getIslandMinion()).open(player);
        }
    }
}
