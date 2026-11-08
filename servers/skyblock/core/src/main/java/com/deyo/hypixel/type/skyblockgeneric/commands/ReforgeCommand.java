package com.deyo.hypixel.type.skyblockgeneric.commands;

import com.deyo.hypixel.commons.skyblock.item.reforge.Reforge;
import com.deyo.hypixel.commons.skyblock.item.reforge.ReforgeLoader;
import com.deyo.hypixel.commons.skyblock.item.reforge.ReforgeType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.generic.utility.MathUtility;
import com.deyo.hypixel.type.skyblockgeneric.item.ItemAttributeHandler;
import com.deyo.hypixel.type.skyblockgeneric.item.components.ReforgableComponent;
import com.deyo.hypixel.type.skyblockgeneric.item.updater.PlayerItemOrigin;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.List;

@CommandParameters(labels = "reforgeitem reforge",
        description = "Reforges the item in the players hand",
        usage = "/reforge",
        permission = Rank.STAFF,
        allowsConsole = false)
public class ReforgeCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            ((SkyBlockPlayer) sender).updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                ItemAttributeHandler itemAttributeHandler = item.getAttributeHandler();
                ReforgeType reforgeType = item.getComponent(ReforgableComponent.class).getReforgeType();

                // Get all valid reforges for this item type
                List<Reforge> validReforges = ReforgeLoader.getReforgesForType(reforgeType);

                if (validReforges.isEmpty()) {
                    sender.sendMessage("<c>No reforges available for this item type!");
                    return;
                }

                // Select a random reforge
                Reforge reforge = validReforges.get(MathUtility.random(0, validReforges.size() - 1));

                try {
                    itemAttributeHandler.setReforge(reforge);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Text.of("<c>{}", e.getMessage()));
                }
            });
        });
    }
}
