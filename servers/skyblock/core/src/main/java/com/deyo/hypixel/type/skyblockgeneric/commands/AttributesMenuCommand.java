package com.deyo.hypixel.type.skyblockgeneric.commands;

import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.hunting.GUIAttributeMenu;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "attributes attributesmenu",
        description = "Opens your Attributes menu",
        usage = "/attributesmenu",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class AttributesMenuCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, _) -> {
            if (!permissionCheck(sender)) return;
            ((SkyBlockPlayer) sender).openView(new GUIAttributeMenu());
        });
    }
}
