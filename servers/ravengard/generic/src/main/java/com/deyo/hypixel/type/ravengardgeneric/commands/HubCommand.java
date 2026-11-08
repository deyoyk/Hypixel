package com.deyo.hypixel.type.ravengardgeneric.commands;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.ravengardgeneric.user.RavengardPlayer;

@CommandParameters(labels = "h hub", description = "Sends the player to the Ravengard hub",
        usage = "/hub", permission = Rank.DEFAULT, allowsConsole = false)
public class HubCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            ((RavengardPlayer) sender).sendTo(ServerType.RAVENGARD_LOBBY);
        });
    }
}
