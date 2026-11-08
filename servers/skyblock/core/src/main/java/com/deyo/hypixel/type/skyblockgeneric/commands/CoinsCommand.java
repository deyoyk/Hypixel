package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointDouble;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDataHandler;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;

@CommandParameters(labels = "coins", description = "Sets your purse balance",
        usage = "/coins <amount>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class CoinsCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentNumber<Double> doubleArgument = ArgumentType.Double("amount").min(0D);

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            player.getSkyblockDataHandler().get(SkyBlockDataHandler.Data.COINS, DatapointDouble.class).setValue(context.get(doubleArgument));

            player.sendMessage("<a>Successfully set coins to to <e>{}<a>.", context.getRaw(doubleArgument));
        }, doubleArgument);
    }
}
