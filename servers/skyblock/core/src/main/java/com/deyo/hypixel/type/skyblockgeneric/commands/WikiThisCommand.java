package com.deyo.hypixel.type.skyblockgeneric.commands;

import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.skyblockgeneric.utility.Wiki;

@CommandParameters(
    labels = "wikithis wikihand wikiinhand",
    description = "Shows page link of the item held towards the official Hypixel Wiki.",
    usage = "/wikithis",
    permission = Rank.DEFAULT,
    allowsConsole = false
)
public class WikiThisCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            Wiki.wikiThis(player);
        });
    }
}
