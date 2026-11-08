package com.deyo.hypixel.type.bedwarsgame.game.v2.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.scoreboard.TeamBuilder;
import com.deyo.hypixel.commons.TeamColorUtil;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsTeam;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.game.game.event.PlayerAssignedTeamEvent;
import com.deyo.hypixel.type.generic.event.EventNodes;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.phase.PhasedEvent;

public class PlayerTeamAssignationListener implements HypixelEventClass {

    @PhasedEvent(node = EventNodes.CUSTOM, requireDataLoaded = false)
    public void onPlayerTeamAssign(PlayerAssignedTeamEvent<BedWarsTeam> event) {
        BedWarsPlayer player = (BedWarsPlayer) event.player();
        player.setTeamName(event.team().getTeamKey());

        TextColor textColor = TextColor.color(event.team().getTeamKey().rgb());
        NamedTextColor color = NamedTextColor.nearestTo(textColor); // possible performance hit?
        player.setTeam(new TeamBuilder(event.team().getName(), MinecraftServer.getTeamManager())
            .prefix(Component.text(event.team().firstLetter(), textColor, TextDecoration.BOLD).appendSpace())
            .teamColor(TeamColorUtil.fromNamedColor(color))
            .build());
        player.getTeam().sendUpdatePacket();
    }

}
