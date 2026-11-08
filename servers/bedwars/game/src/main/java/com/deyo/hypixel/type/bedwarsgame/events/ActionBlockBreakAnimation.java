package com.deyo.hypixel.type.bedwarsgame.events;

import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.play.BlockBreakAnimationPacket;
import com.deyo.hypixel.type.bedwarsgame.TypeBedWarsGameLoader;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;

public class ActionBlockBreakAnimation implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false, isAsync = true) // should it be async? - ARI
    public void run(PlayerPacketOutEvent event) {
        BedWarsPlayer player = (BedWarsPlayer) event.getPlayer();
        BedWarsGame game = TypeBedWarsGameLoader.getGameById(player.getGameId());
        if (game == null || !game.getReplayManager().isRecording()) {
            return;
        }

        ServerPacket packet = event.getPacket();
        if (packet instanceof BlockBreakAnimationPacket p) {
            game.getReplayManager().recordBlockBreakAnimation(p);
        }
    }

}
