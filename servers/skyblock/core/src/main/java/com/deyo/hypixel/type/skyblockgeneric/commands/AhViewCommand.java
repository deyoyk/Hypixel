package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.minecraft.ArgumentUUID;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.auction.GUIAuctionBrowser;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.auction.GUIAuctionViewItem;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.UUID;

@CommandParameters(labels = "auctionview ahview",
        description = "Views an auction",
        usage = "/ahview <uuid>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class AhViewCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentUUID uuid = new ArgumentUUID("uuid");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            UUID ah = context.get(uuid);

            new GUIAuctionViewItem(ah, new GUIAuctionBrowser()).open((SkyBlockPlayer) sender);
        }, uuid);
    }
}
