package com.deyo.hypixel.type.skyblockgeneric.commands;

import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.skyblockgeneric.item.ConfigurableSkyBlockItem;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import org.tinylog.Logger;

@CommandParameters(labels = "displayconfigids",
        description = "Prints out the item IDs from the config into the console",
        usage = "/displayconfigids",
        permission = Rank.STAFF,
        allowsConsole = false)
public class PrintConfigIdsCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            sender.sendMessage("<a>Available Item IDs:");
            for (String id : ConfigurableSkyBlockItem.getIDs()) {
                sender.sendMessage(Text.of("<7>{}", id));
                Logger.info(id);
            }
        });
    }
}
