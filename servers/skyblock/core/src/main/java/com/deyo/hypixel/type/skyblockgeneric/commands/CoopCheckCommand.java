package com.deyo.hypixel.type.skyblockgeneric.commands;

import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.data.monogdb.CoopDatabase;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.coop.GUICoopInviteSender;
import com.deyo.hypixel.type.skyblockgeneric.gui.inventories.coop.GUICoopInviteTarget;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "cooperativecheck",
        description = "Checks outgoing invites",
        usage = "/coop",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class CoopCheckCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            CoopDatabase.Coop coop = CoopDatabase.getFromMember(player.getUuid());

            if (coop == null) {
                player.sendMessage("<c>You don't have any co-op invites!");
                return;
            }

            if (coop.members().contains(player.getUuid())) {
                player.sendMessage("<b>[Co-op] <c>You are already in a co-op!");
                return;
            }

            if (coop.isOriginator(player.getUuid())) {
                new GUICoopInviteSender(coop).open(player);
            } else {
                new GUICoopInviteTarget(coop).open(player);
            }
        });
    }
}
