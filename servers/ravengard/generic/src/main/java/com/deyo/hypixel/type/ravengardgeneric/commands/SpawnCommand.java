package com.deyo.hypixel.type.ravengardgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.ravengardgeneric.entity.animation.RavengardMobClip;
import com.deyo.hypixel.type.ravengardgeneric.entity.mob.RavengardMob;
import com.deyo.hypixel.type.ravengardgeneric.user.RavengardPlayer;

import java.util.List;

@CommandParameters(
        labels = "spawn",
        description = "Spawns a Ravengard mob at your position",
        usage = "/spawn <mob>",
        permission = Rank.STAFF,
        allowsConsole = false)
public class SpawnCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        var mobArg = ArgumentType.Word("mob").setSuggestionCallback((sender, context, suggestion) ->
                RavengardMobClip.available().forEach(name -> suggestion.addEntry(new SuggestionEntry(name))));

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            RavengardPlayer player = (RavengardPlayer) sender;
            String name = context.get(mobArg);
            List<String> available = RavengardMobClip.available();
            if (!available.contains(name)) {
                player.sendMessage("<c>Unknown mob. Options: {}", String.join(", ", available));
                return;
            }
            RavengardMobClip clip = RavengardMobClip.load(name);
            RavengardMob mob = new RavengardMob(clip, player.getPosition());
            mob.spawnMob(player.getInstance());
            player.sendMessage("<a>Spawned <f>{}<a>.", name);
        }, mobArg);
    }
}
