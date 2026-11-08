package com.deyo.hypixel.type.generic.event.actions.data;

import lombok.SneakyThrows;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.protocol.objects.proxy.to.FinishedWithPlayerProtocol;
import com.deyo.hypixel.commons.redis.RedisClient;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.data.domain.PlayerDataService;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.EventPhase;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.user.flow.PlayerFlow;
import com.deyo.hypixel.type.generic.utility.MathUtility;

public class ActionPlayerDataSave implements HypixelEventClass {
    @SneakyThrows
    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true, phase = EventPhase.PERSIST)
    public void run(PlayerDisconnectEvent event) {
        HypixelPlayer player = (HypixelPlayer) event.getPlayer();
        ServerType type = HypixelConst.getTypeLoader().getType();
        PlayerFlow.run(player, "data/save", () -> {
            PlayerDataService.saveAndUnloadAll(type, player);
            RedisClient.requestProxy(new FinishedWithPlayerProtocol(),
                    new FinishedWithPlayerProtocol.Request(player.getUuid().toString()));
            MathUtility.delay(() -> HypixelConst.getTypeLoader().getTablistManager().deleteTablistEntries(player), 5);
        });
    }
}
