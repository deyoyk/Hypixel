package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "i item sbitem",
        description = "Gives an item to the player",
        usage = "/item <item> [amount]",
        permission = Rank.STAFF,
        allowsConsole = false)
public class ItemCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<ItemType> itemArgument = ArgumentType.Enum("item", ItemType.class);
        ArgumentInteger amountArgument = ArgumentType.Integer("amount");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            final ItemType itemTypeLinker = context.get(itemArgument);

            SkyBlockItem item = new SkyBlockItem(itemTypeLinker);
            ((SkyBlockPlayer) sender).addAndUpdateItem(item);

            sender.sendMessage(Text.of("<a>Given you item <e>{} <a>.", itemTypeLinker.name()));
        }, itemArgument);

        command.addSyntax((sender, context) -> {
            final ItemType itemTypeLinker = context.get(itemArgument);
            final int amount = context.get(amountArgument);

            SkyBlockItem item = new SkyBlockItem(itemTypeLinker);
            item.setAmount(amount);
            ((SkyBlockPlayer) sender).addAndUpdateItem(item);

            sender.sendMessage(Text.of("<a>Given you item <e>{}<8> x{}<a>.", itemTypeLinker.name(), amount));
        }, itemArgument, amountArgument);
    }
}
