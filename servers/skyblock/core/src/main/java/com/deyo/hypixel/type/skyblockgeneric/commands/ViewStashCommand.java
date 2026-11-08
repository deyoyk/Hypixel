package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentWord;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.stash.GUIStashItem;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.stash.GUIStashMaterial;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "viewstash",
        description = "View your stash",
        usage = "/viewstash [item|material]",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class ViewStashCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentWord typeArg = new ArgumentWord("type").from("item", "material");

        // No args - open item stash by default
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            new GUIStashItem().open(player);
        });

        // With type argument
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            String type = context.get(typeArg);

            if (type.equalsIgnoreCase("material")) {
                new GUIStashMaterial().open(player);
            } else {
                new GUIStashItem().open(player);
            }
        }, typeArg);
    }
}
