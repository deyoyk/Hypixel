package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.BestiaryMob;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.MobRegistry;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;

@CommandParameters(labels = "updatebestiary",
        description = "Updates the bestiary of a player",
        usage = "/setbestiary <mob> <amount>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class SetBestiaryCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentString mobArgument = new ArgumentString("mob");
        ArgumentInteger amountArgument = new ArgumentInteger("amount");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            String mobId = context.get(mobArgument);
            BestiaryMob mob = MobRegistry.getMobById(mobId);

            if (mob == null) {
                player.sendMessage("<c>Unknown mob: {}", mobId);
                return;
            }

            int amount = context.get(amountArgument);
            player.getBestiaryData().set(mob, amount);

            player.sendMessage("<a>Set bestiary kills for <e>{}<a> to <e>{}<a>.", mob.getDisplayName(), amount);
        }, mobArgument, amountArgument);
    }
}

