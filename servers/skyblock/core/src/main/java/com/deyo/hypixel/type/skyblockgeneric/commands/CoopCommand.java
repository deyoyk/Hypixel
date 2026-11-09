package com.deyo.hypixel.type.skyblockgeneric.commands;

import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import com.deyo.hypixel.proxyapi.ProxyPlayerSet;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.data.mongodb.ProfilesDatabase;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skyblockgeneric.data.monogdb.CoopInviteService;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@CommandParameters(labels = "cooperative coop",
        description = "Primary coop command",
        usage = "/coop",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class CoopCommand extends HypixelCommand {
    @Override
    public void registerUsage(MinestomCommand command) {
        ArgumentStringArray args = ArgumentType.StringArray("player");

        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;

            SkyBlockPlayer player = (SkyBlockPlayer) sender;

            if (CoopInviteService.hasExistingInvite(player)) return;

            player.sendMessage("<c>You don't have an outgoing co-op invite!");
            player.sendMessage("<e>Use <b>/coop \\<player 1> \\<player 2>... <e>to create one!");
            player.sendMessage("<e>Use <a>/coopadd \\<player> <e>to add a player to your current co-op!");
        });

        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = (SkyBlockPlayer) sender;
            String[] players = context.get(args);

            if (CoopInviteService.hasExistingInvite(player)) return;

            player.sendMessage("<7>Validating invite...");

            ProxyPlayerSet chosenPlayers;

            try {
                chosenPlayers = new ProxyPlayerSet(Arrays.stream(players).map(
                        ProfilesDatabase::fetchUUID
                ).collect(Collectors.toList()));
            } catch (Exception e) {
                player.sendMessage("<b>[Co-op] <c>One or more of the players you specified are not online!");
                return;
            }

            if (chosenPlayers.getPlayers().stream().anyMatch(Objects::isNull)) {
                player.sendMessage("<b>[Co-op] <c>One or more of the players you specified are not online!");
                return;
            }

            if (chosenPlayers.asProxyPlayers().stream().anyMatch(player1 -> !player1.isOnline().join())) {
                player.sendMessage("<b>[Co-op] <c>One or more of the players you specified are not online!");
                return;
            }

            CoopInviteService.invite(player, chosenPlayers.asProxyPlayers(), Arrays.asList(players));
        }, args);
    }
}
