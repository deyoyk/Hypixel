package com.deyo.hypixel.type.bedwarsgame.game.v2.listener;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import com.deyo.hypixel.commons.bedwars.map.BedWarsMapsConfig;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.bedwarsgame.events.custom.BedDestroyedEvent;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class BedBrokenListener implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onBedDestroyed(BedDestroyedEvent event) {
        BedWarsGame game = (BedWarsGame) event.game();
        BedWarsMapsConfig.TeamKey teamKey = event.teamKey();
        if (game == null) return;

        BedWarsPlayer destroyer = event.destroyer();
        BedWarsMapsConfig.TeamKey destroyerTeamKey = destroyer.getTeamKey();
        if (destroyerTeamKey == null) {
            throw new IllegalStateException("Destroyer team key is null for player " + destroyer.getUsername());
        }

        game.broadcastMessage(Text.of("\n<f><l>BED DESTRUCTION > </l>{} <7>has been destroyed by {}!\n",
            Text.of("<color:{}>{} Bed", teamKey.chatColor(), teamKey.getName()),
            Text.of("<color:{}>{}", destroyerTeamKey.chatColor(), destroyer.getUsername())));

        for (BedWarsPlayer player : game.getPlayers()) {
            player.playSound(Sound.sound(Key.key("minecraft:entity.wither.death"),
                Sound.Source.MASTER, 1f, 1f), Sound.Emitter.self());
        }

        game.checkWinConditions();
    }

}
