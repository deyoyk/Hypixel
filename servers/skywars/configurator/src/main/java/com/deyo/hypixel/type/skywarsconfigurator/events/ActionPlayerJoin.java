package com.deyo.hypixel.type.skywarsconfigurator.events;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import org.tinylog.Logger;

public class ActionPlayerJoin implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, phase = EventPhase.CONNECT)
    public void run(AsyncPlayerConfigurationEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        event.setSpawningInstance(HypixelConst.getEmptyInstance());
        Logger.info("Player " + player.getUsername() + " joined SkyWars Configurator");
        player.setRespawnPoint(HypixelConst.getTypeLoader()
                .getLoaderValues()
                .spawnPosition()
                .apply(player.getOriginServer())
        );
    }
}
