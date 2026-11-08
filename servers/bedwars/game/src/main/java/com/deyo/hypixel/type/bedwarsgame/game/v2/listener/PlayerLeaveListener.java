package com.deyo.hypixel.type.bedwarsgame.game.v2.listener;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.game.game.event.PlayerLeaveGameEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

import java.util.Random;

public class PlayerLeaveListener implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onPlayerLeave(PlayerLeaveGameEvent event) {
        BedWarsPlayer player = (BedWarsPlayer) event.player();
        player.sendTo(ServerType.BEDWARS_LOBBY);
        BedWarsGame game = (BedWarsGame) event.game();
        if (game == null) return;
        if (!game.getState().isWaiting()) return;

        game.broadcastMessage(Text.of("<k>{} <e>has quit!",
            player.getFakeUuid().toString().substring(0, new Random().nextInt(10) + 4)));
    }
}
