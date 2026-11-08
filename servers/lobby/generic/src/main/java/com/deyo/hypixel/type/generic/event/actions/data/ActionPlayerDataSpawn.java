package com.deyo.hypixel.type.generic.event.actions.data;

import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.data.domain.PlayerDataService;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.user.flow.PlayerFlow;

public class ActionPlayerDataSpawn implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER_DATA, isAsync = true, phase = EventPhase.POST_SPAWN)
    public void run(PlayerSpawnEvent event) {
        if (!event.isFirstSpawn()) return;

        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        ServerType type = HypixelConst.getTypeLoader().getType();
        PlayerFlow.run(player, "data/apply", () -> PlayerDataService.applyAll(type, player));
    }
}
