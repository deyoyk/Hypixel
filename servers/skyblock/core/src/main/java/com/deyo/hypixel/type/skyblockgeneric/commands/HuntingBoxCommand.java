package com.deyo.hypixel.type.skyblockgeneric.commands;

import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.hunting.GUIHuntingBox;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "huntingbox",
        description = "Opens your Hunting Box",
        usage = "/huntingbox",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class HuntingBoxCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            ((SkyBlockPlayer) sender).openView(new GUIHuntingBox());
        });
    }
}
