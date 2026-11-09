package com.deyo.hypixel.type.lobby.commands;

import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.lobby.gui.GUIAchievementsMenu;

@CommandParameters(
        labels = "achievements",
        description = "Opens the achievements menu",
        usage = "/achievements",
        permission = Rank.DEFAULT,
        allowsConsole = false
)
public class AchievementsCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            new GUIAchievementsMenu().open((HypixelPlayer) sender);
        });
    }
}
