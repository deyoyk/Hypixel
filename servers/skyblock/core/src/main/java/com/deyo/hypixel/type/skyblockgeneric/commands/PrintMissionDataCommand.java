package com.deyo.hypixel.type.skyblockgeneric.commands;
import com.deyo.hypixel.commons.text.Text;

import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.skyblockgeneric.mission.MissionData;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;

@CommandParameters(labels = "displaymissiondata",
        description = "Displays the mission data of a player",
        usage = "/displaymissiondata",
        permission = Rank.STAFF,
        allowsConsole = false)
public class PrintMissionDataCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            MissionData data = player.getMissionData();

            player.sendMessage("<a>Mission Data:");
            player.sendMessage("<7>Active Missions:");
            data.getActiveMissions().forEach((mission) -> {
                player.sendMessage("<e>{}<7>:", mission.toString());
                mission.getCustomData().forEach((key, value) -> {
                    player.sendMessage("<7>{}<e>: {}", key, value);
                });
            });

            player.sendMessage("<7>Completed Missions:");
            data.getCompletedMissions().forEach((mission) -> {
                player.sendMessage("<e>{}", mission.toString());
                mission.getCustomData().forEach((key, value) -> {
                    player.sendMessage("<7>{}<e>: {}", key, value);
                });
            });
        });
    }
}
