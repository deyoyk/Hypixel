package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.EnchantmentType;
import com.deyo.hypixel.type.skyblockgeneric.enchantment.SkyBlockEnchantment;
import com.deyo.hypixel.type.skyblockgeneric.item.ItemAttributeHandler;
import com.deyo.hypixel.type.skyblockgeneric.item.updater.PlayerItemOrigin;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "ench enchant",
        description = "Enchants the contents o the players hand",
        usage = "/enchant <enchantment_type> <level>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class EnchantCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<EnchantmentType> typeArgument =
                ArgumentType.Enum("enchantment_type", EnchantmentType.class);
        ArgumentInteger level = ArgumentType.Integer("level");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            ((SkyBlockPlayer) sender).updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                ItemAttributeHandler itemAttributeHandler = item.getAttributeHandler();
                itemAttributeHandler.addEnchantment(
                        SkyBlockEnchantment.builder()
                                .level(context.get(level))
                                .type(context.get(typeArgument))
                                .build()
                );
                sender.sendMessage("<a>Your item has been enchanted");
            });
        }, typeArgument, level);
    }
}
