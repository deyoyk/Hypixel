package com.deyo.hypixel.type.bedwarslobby.events;

import net.minestom.server.event.player.PlayerSpawnEvent;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.protocol.objects.orchestrator.RejoinGameProtocol;
import com.deyo.hypixel.proxyapi.ProxyService;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.utility.ScheduleUtility;

public class ActionRejoinCheck implements HypixelEventClass {
    private static final ProxyService ORCHESTRATOR = new ProxyService(ServiceType.ORCHESTRATOR);

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false, isAsync = true)
    public void run(PlayerSpawnEvent event) {
        final HypixelPlayer player = (HypixelPlayer) event.getPlayer();

        // Delay slightly to ensure player is fully spawned
        ScheduleUtility.delay(() -> {
            // Check if player has an active game to rejoin
            RejoinGameProtocol.RejoinGameRequest request =
                    new RejoinGameProtocol.RejoinGameRequest(player.getUuid());

            ORCHESTRATOR.handleRequest(request).thenAccept(response -> {
                if (response instanceof RejoinGameProtocol.RejoinGameResponse resp
                        && resp.hasActiveGame()) {
                    // Show rejoin message
                    player.sendMessage("<c>You have a game currently ongoing! <click:run:'/rejoin'>Click here to rejoin.</click>");
                }
            }).exceptionally(throwable -> {
                // Silently fail - don't bother the player with errors on this check
                return null;
            });
        }, 500); // 500ms delay
    }
}
