package com.deyo.hypixel.type.skyblockgeneric.commands;

import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.skyblockgeneric.item.ItemAttributeHandler;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.item.components.MinionComponent;
import com.deyo.hypixel.type.skyblockgeneric.item.updater.PlayerItemOrigin;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;

@CommandParameters(labels = "mithrilinfuse",
        description = "Mithril Infuses the minion in the players hand",
        usage = "/mithrilinfuse",
        permission = Rank.STAFF,
        allowsConsole = false)
public class MithrilInfuseCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            SkyBlockPlayer player = ((SkyBlockPlayer) sender);
            if (!(new SkyBlockItem(player.getItemInMainHand()).hasComponent(MinionComponent.class))) {
                player.sendMessage("<c>Mithril Infusions can only be applied to minions.");
                return;
            }
            player.updateItem(PlayerItemOrigin.MAIN_HAND, (item) -> {
                ItemAttributeHandler itemAttributeHandler = item.getAttributeHandler();
                try {
                    itemAttributeHandler.setMithrilInfused(true);
                    player.sendMessage("<a>Mithril Infusion applied to {}.", item.getDisplayName());
                } catch (IllegalArgumentException e) {
                    player.sendMessage("<c>{}", e.getMessage());
                }
            });
        });
    }
}
