package com.deyo.hypixel.type.generic.event.actions.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.data.domain.PlayerDataService;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.resourcepack.ResourcePackManager;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.user.flow.PlayerFlow;

public class ActionPlayerDataLoad implements HypixelEventClass {

    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER_DATA, requireDataLoaded = false, phase = EventPhase.LOAD_DATA)
    public void run(AsyncPlayerConfigurationEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        ServerType type = HypixelConst.getTypeLoader().getType();
        PlayerFlow.run(player, "data/load", () -> {
            PlayerDataService.loadAll(type, player.getUuid());
            PlayerDataService.attachAll(type, player);
        });

        ResourcePackManager packManager = HypixelConst.getResourcePackManager();
        if (packManager != null) {
            PlayerFlow.run(player, "data/resource-pack", () -> packManager.sendPackBlocking(player, 25));
        }
    }
}
