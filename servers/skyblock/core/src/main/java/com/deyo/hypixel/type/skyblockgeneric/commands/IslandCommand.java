package com.deyo.hypixel.type.skyblockgeneric.commands;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "is island",
        description = "Sends the player to their island",
        usage = "/is",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class IslandCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = ((SkyBlockPlayer) sender);
            player.sendTo(ServerType.SKYBLOCK_ISLAND);
        });
    }
}
