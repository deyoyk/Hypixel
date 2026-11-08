package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.recipe.GUIRecipe;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(description = "Opens up a recipe GUI",
        usage = "/viewrecipe <recipe>",
        permission = Rank.DEFAULT,
    labels = "vr viewrecipe",
        allowsConsole = false)
public class ViewRecipeCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<ItemType> itemArgument = ArgumentType.Enum("item", ItemType.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            final ItemType item = context.get(itemArgument);
            ((SkyBlockPlayer) sender).openView(new GUIRecipe(item));
        }, itemArgument);
    }
}
