package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.GUICreative;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "itemlist e",
        description = "Open the E menu",
        usage = "/e",
        permission = Rank.STAFF,
        allowsConsole = false)
public class ItemListCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            ((SkyBlockPlayer) sender).openView(new GUICreative(), GUICreative.createInitialState());
        });

        ArgumentString lookup = new ArgumentString("lookup");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String lookupValue = context.get(lookup);
            ((SkyBlockPlayer) sender).openView(new GUICreative(), GUICreative.createInitialState(lookupValue, 0));
        }, lookup);
    }
}
