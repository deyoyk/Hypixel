package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.collection.CollectionCategories;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.GUISkyBlockProfile;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(description = "Opens up a collections GUI",
        usage = "/viewcollection <collection>",
        permission = Rank.DEFAULT,
    labels = "vc viewcollection",
        allowsConsole = false)
public class ViewCollectionCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<ItemType> itemArgument = ArgumentType.Enum("item", ItemType.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            final ItemType itemTypeLinker = context.get(itemArgument);

            if (CollectionCategories.getCategory(itemTypeLinker) == null) {
                sender.sendMessage("<c>This item does not have a collection!");
                return;
            }

            ((SkyBlockPlayer) sender).openView(new GUISkyBlockProfile());
        }, itemArgument);
    }
}
