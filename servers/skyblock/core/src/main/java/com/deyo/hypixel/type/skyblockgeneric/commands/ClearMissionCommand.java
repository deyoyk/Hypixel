package com.deyo.hypixel.type.skyblockgeneric.commands;

import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointMissionData;
import com.deyo.hypixel.type.skyblockgeneric.mission.MissionData;
import com.deyo.hypixel.type.skyblockgeneric.mission.missions.MissionBreakLog;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;

@CommandParameters(description = "Clears your missions",
        usage = "/missionempty",
        permission = Rank.STAFF,
        labels = "clearmission",
        allowsConsole = false)
public class ClearMissionCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            sender.sendMessage("<a>Your missions have been cleared.");
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.MISSION_DATA, DatapointMissionData.class)
                    .setValue(new MissionData());
            player.getMissionData().startMission(MissionBreakLog.class);
        });
    }
}
