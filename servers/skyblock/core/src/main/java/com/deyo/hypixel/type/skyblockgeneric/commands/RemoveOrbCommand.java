package com.deyo.hypixel.type.skyblockgeneric.commands;

import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.skyblockgeneric.data.crystals.CrystalCatalog;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;

@CommandParameters(labels = "deleteorb",
        description = "Deletes orbs at the player's location.",
        usage = "/removeorb",
        permission = Rank.STAFF,
        allowsConsole = false)
public class RemoveOrbCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            CrystalCatalog.removeCrystals(((SkyBlockPlayer) sender).getPosition(), 1.5);
        });
    }
}
