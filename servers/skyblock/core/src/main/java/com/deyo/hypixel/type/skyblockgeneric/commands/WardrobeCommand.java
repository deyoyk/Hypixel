package com.deyo.hypixel.type.skyblockgeneric.commands;

import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.sbmenu.GUIWardrobe;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(description = "Open your Wardrobe", usage = "/wardrobe", permission = Rank.DEFAULT,
    labels = "wd wardrobe", allowsConsole = false)
public class WardrobeCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.setDefaultExecutor((sender, _) -> {
            if (!permissionCheck(sender)) return;
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            // doesn't seem to be a thing in the loadout update
            /*if (player.getSkyBlockExperience().getLevel().getLevel() < 5) {
                player.sendMessage("<c>You must be SkyBlock Level 5 to use the Wardrobe!");
                return;
            }*/
            player.openView(new GUIWardrobe());
        });
    }
}
