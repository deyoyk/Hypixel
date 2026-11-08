package com.deyo.hypixel.type.skyblockgeneric.commands;

import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(description = "Toggles whether or not you are in build mode",
        usage = "/build",
        permission = Rank.STAFF,
    labels = "buildmode build",
        allowsConsole = false)
public class BuildCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            player.setBypassBuild(!player.isBypassBuild());

            sender.sendMessage(Text.of("<a>Build mode has been " + (player.isBypassBuild() ? "<a>ENABLED" : "<c>DISABLED") + "<a>."));
        });
    }
}
