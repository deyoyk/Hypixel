package com.deyo.hypixel.type.bedwarsgame.events;

import net.minestom.server.event.player.PlayerHandAnimationEvent;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;

public class ActionPlayerArmSwing implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.PLAYER, requireDataLoaded = false)
    public void run(PlayerHandAnimationEvent event) {
        if (!(event.getPlayer() instanceof BedWarsPlayer player)) {
            return;
        }

        BedWarsGame game = player.getGame();
        if (game == null || game.getReplayManager() == null) {
            return;
        }

        if (!game.getReplayManager().isRecording()) {
            return;
        }

        var dispatcher = game.getReplayManager().getEntityLifecycleDispatcher();
        if (dispatcher != null) {
            boolean mainHand = event.getHand().name().equals("MAIN");
            dispatcher.recordArmSwing(player.getEntityId(), mainHand);
        }
    }
}
