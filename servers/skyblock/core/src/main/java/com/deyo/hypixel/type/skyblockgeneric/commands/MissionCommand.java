package com.deyo.hypixel.type.skyblockgeneric.commands;

import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.mission.MissionData;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "missiondata mission",
        description = "Collects mission information",
        usage = "/mission",
        permission = Rank.STAFF,
        allowsConsole = false)
public class MissionCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            MissionData data = ((SkyBlockPlayer) sender).getMissionData();

            sender.sendMessage(Text.of("Active Missions: {}", data.getActiveMissions().size()));
            sender.sendMessage(Text.of("Completed Missions: {}", data.getCompletedMissions().size()));

            data.getCompletedMissions().forEach(mission -> {
                sender.sendMessage(Text.of("Completed Mission: {}", mission.getMissionID()));
            });
            data.getActiveMissions().forEach(mission -> {
                sender.sendMessage(Text.of("Active Mission: {}", mission.getMissionID()));
            });
        });
    }
}