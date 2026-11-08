package com.deyo.hypixel.type.bedwarsgame.game.v2.listener;

import net.kyori.adventure.text.format.TextColor;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsTeam;
import com.deyo.hypixel.type.bedwarsgame.replay.BedWarsReplayManager;
import com.deyo.hypixel.type.bedwarsgame.stats.BedWarsStatsRecorder;
import com.deyo.hypixel.type.game.game.event.TeamEliminatedEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;

import java.util.Optional;

public class TeamEliminationListener implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void run(TeamEliminatedEvent<BedWarsTeam> event) {
        BedWarsTeam team = event.team();
        TextColor teamColor = team.getColor();
        String teamName = team.getName();

        BedWarsGame game = (BedWarsGame) event.game();
        team.getPlayerIds().stream()
            .map(game::getPlayer)
            .flatMap(Optional::stream)
            .forEach(player -> {
                BedWarsStatsRecorder.recordBedLost(player, game.getGameType());
                BedWarsStatsRecorder.recordLoss(player, game.getGameType());
            });

        game.broadcastMessage(Text.empty());
        game.broadcastMessage(Text.of("<f><l>TEAM ELIMINATED > </l>{} <c>has been eliminated!",
            Text.of("<color:{}>{}", teamColor, teamName)));
        game.broadcastMessage(Text.empty());

        BedWarsReplayManager replayManager = game.getReplayManager();

        // Record to replay
        if (replayManager.isRecording()) {
            replayManager.recordTeamElimination(team.getTeamKey());
        }
    }
}
