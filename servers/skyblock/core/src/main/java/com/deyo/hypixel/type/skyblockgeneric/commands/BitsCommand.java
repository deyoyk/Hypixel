package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointInteger;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(description = "Sets your bits balance",
    labels = "bit bits",
        usage = "/bits <amount>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class BitsCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentNumber<Integer> bitsArgument = ArgumentType.Integer("amount").min(0);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            player.getSkyblockDataHandler().get(com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler.Data.BITS, DatapointInteger.class).setValue(context.get(bitsArgument));
            player.sendMessage("<a>Successfully set bits to to <e>{}<a>.", context.getRaw(bitsArgument));
        }, bitsArgument);
    }
}
