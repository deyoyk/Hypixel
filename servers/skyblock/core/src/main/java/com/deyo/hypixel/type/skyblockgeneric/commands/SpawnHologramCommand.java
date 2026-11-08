package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Entity;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.entity.hologram.HologramEntity;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

@CommandParameters(labels = "spawnholo spawnhologram",
        description = "Rank command",
        usage = "/spawnhologram <text>",
        permission = Rank.STAFF,
    allowsConsole = false
)
public class SpawnHologramCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentStringArray nameArgument = ArgumentType.StringArray("name");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            String[] nameArray = context.get(nameArgument);
            String name = String.join(" ", nameArray);
            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            Entity entity = new HologramEntity(name);
            entity.setAutoViewable(true);
            entity.setInstance(HypixelConst.getInstanceContainer(), player.getPosition());
        }, nameArgument);
    }
}
