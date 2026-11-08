package com.deyo.hypixel.type.generic.command.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.data.HypixelDataHandler;
import com.deyo.hypixel.type.generic.data.datapoints.DatapointLocale;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;

import java.util.Locale;

@CommandParameters(labels = "localedata",
    description = "Locale Data",
    usage = "/localedata",
    permission = Rank.STAFF,
    allowsConsole = false)
public class LocaleDataCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        command.setDefaultExecutor((sender, context) -> {
            if (!permissionCheck(sender)) return;

            if (!(sender instanceof HypixelPlayer player)) return;

            player.sendMessage("<e>Your locale is currently set to <c>{}<e>.", player.getLocale().getDisplayName());
        });

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            if (!(sender instanceof HypixelPlayer player)) return;

            HypixelDataHandler handler = player.getDataHandler();

            Locale loc = handler.get(HypixelDataHandler.Data.LOCALE, DatapointLocale.class).getValue().getCurrentLocale().getLocale();
            player.setLocale(loc);

            player.sendMessage("<e>Your locale is now set to <c>{}<e>.", loc.getDisplayName());
        }, ArgumentType.Literal("update"));
    }
}
