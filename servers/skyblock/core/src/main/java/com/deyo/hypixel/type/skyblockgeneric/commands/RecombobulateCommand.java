package com.deyo.hypixel.type.skyblockgeneric.commands;

import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.skyblockgeneric.item.ItemAttributeHandler;
import com.deyo.hypixel.type.skyblockgeneric.item.updater.PlayerItemOrigin;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;

@CommandParameters(labels = "recomb",
        description = "Recombobulates the item in the players hand",
        usage = "/recombobulate",
        permission = Rank.STAFF,
        allowsConsole = false)
public class RecombobulateCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            ((SkyBlockPlayer) sender).updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                ItemAttributeHandler itemAttributeHandler = item.getAttributeHandler();
                if (itemAttributeHandler.isPet()) {
                    sender.sendMessage("<c>Pets cannot be recombobulated!");
                    return;
                }
                itemAttributeHandler.setRecombobulated(!itemAttributeHandler.isRecombobulated());
                sender.sendMessage(Text.of("<a>Recombobulated: <d>{}", itemAttributeHandler.isRecombobulated()));
            });
        });
    }
}