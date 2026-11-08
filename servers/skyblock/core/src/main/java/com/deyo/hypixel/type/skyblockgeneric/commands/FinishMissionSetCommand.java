package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.skyblockgeneric.mission.MissionSet;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;

import java.util.Arrays;

@CommandParameters(labels = "clearmissionset completemissionset",
        description = "Clears the mission set given",
        usage = "/finishmissionset <set>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class FinishMissionSetCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<MissionSet> set = ArgumentType.Enum("set", MissionSet.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            MissionSet missionSet = context.get(set);

            Arrays.stream(missionSet.getMissions()).forEach(mission -> {
                try {
                    ((SkyBlockPlayer) sender).getMissionData().endMission(mission);
                } catch (Exception e) {}
            });

            sender.sendMessage(Text.of("<a>Successfully finished mission set <e>{}", missionSet.name()));
        }, set);
    }
}
