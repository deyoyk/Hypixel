package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.PlayerHand;
import com.deyo.hypixel.commons.skyblock.item.attribute.ItemAttribute;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;

@CommandParameters(description = "Modifies the NBT of the item in your hand",
        usage = "/changenbt <key> <value>",
        permission = Rank.STAFF,
        labels = "editnbt",
        allowsConsole = false)
public class ChangeNBTCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString keyArgument = ArgumentType.String("key");
        ArgumentString valueArgument = ArgumentType.String("value");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String key = context.get(keyArgument);
            String value = context.get(valueArgument);

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            SkyBlockItem item = new SkyBlockItem(player.getItemInHand(PlayerHand.MAIN));

            ItemAttribute attribute = item.getAttribute(key);
            attribute.setValue(attribute.loadFromString(value));

            player.setItemInHand(PlayerHand.MAIN, item.getItemStack());
            player.sendMessage("<a>Successfully changed NBT key <e>{} <a>to <e>{} <a>.", key, value);
        }, keyArgument, valueArgument);
    }
}
