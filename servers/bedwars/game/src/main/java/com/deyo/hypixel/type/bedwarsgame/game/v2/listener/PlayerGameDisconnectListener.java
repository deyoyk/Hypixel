package com.deyo.hypixel.type.bedwarsgame.game.v2.listener;

import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.game.game.event.PlayerDisconnectGameEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;

public class PlayerGameDisconnectListener implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onPlayerDisconnect(PlayerDisconnectGameEvent event) {
        BedWarsPlayer player = (BedWarsPlayer) event.player();
        BedWarsGame game = player.getGame();
        if (game != null) {
            Text name = game.getPlayerTeam(player.getUuid())
                .map(team -> Text.of("<color:{}>{}", team.getColor(), player.getUsername()))
                .orElseGet(() -> Text.literal(player.getUsername()));

            game.broadcastMessage(Text.of("<7>{} disconnected.", name));
        }
    }

}
