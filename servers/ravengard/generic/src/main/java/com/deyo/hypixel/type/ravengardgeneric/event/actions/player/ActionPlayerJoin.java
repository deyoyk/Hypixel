package com.deyo.hypixel.type.ravengardgeneric.event.actions.player;

import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.ravengardgeneric.user.RavengardPlayer;
import org.tinylog.Logger;

public class ActionPlayerJoin implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, phase = EventPhase.GAMEPLAY)
    public void run(AsyncPlayerConfigurationEvent event) {
        RavengardPlayer player = (RavengardPlayer) event.getPlayer();

        event.setSpawningInstance(HypixelConst.getEmptyInstance());
        player.setRespawnPoint(HypixelConst.getTypeLoader()
                .getLoaderValues()
                .spawnPosition()
                .apply(player.getOriginServer())
        );

        Logger.info("Player {} joined Ravengard from {}", player.getUsername(), player.getOriginServer());
    }
}
