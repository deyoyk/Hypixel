package com.deyo.hypixel.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentEnum;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;

@CommandParameters(labels = "transfer sendto",
        description = "Transfers a player to another server",
        usage = "/sendto <server_type>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class SendToCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentEnum<ServerType> serverType = new ArgumentEnum<>("server_type", ServerType.class);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            ServerType type = context.get(serverType);
            HypixelPlayer player = (HypixelPlayer) sender;

            player.sendTo(type, true);
        }, serverType);
    }
}
