package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "experience xp",
        description = "Sets your experience levels",
        usage = "/xp <level>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class XpCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentInteger levelArgument = ArgumentType.Integer("level");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            int level = context.get(levelArgument);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            player.setLevel(level);
        }, levelArgument);
    }
}
