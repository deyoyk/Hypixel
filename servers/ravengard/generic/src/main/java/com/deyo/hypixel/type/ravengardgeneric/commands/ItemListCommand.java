package com.deyo.hypixel.type.ravengardgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.gui.v2.ViewNavigator;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.ravengardgeneric.gui.GUIItemList;
import com.deyo.hypixel.type.ravengardgeneric.user.RavengardPlayer;

@CommandParameters(
        labels = "items e",
        description = "Browse every implemented Ravengard item",
        usage = "/items [search]",
        permission = Rank.STAFF,
        allowsConsole = false)
public class ItemListCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            RavengardPlayer player = (RavengardPlayer) sender;
            ViewNavigator.get(player).push(new GUIItemList(null, 0));
        });

        ArgumentString lookup = new ArgumentString("lookup");
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            RavengardPlayer player = (RavengardPlayer) sender;
            ViewNavigator.get(player).push(new GUIItemList(context.get(lookup), 0));
        }, lookup);
    }
}
