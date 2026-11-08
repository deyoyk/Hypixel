package com.deyo.hypixel.type.skywarsgame.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.skywarsgame.TypeSkywarsGameLoader;
import com.deyo.hypixel.type.skywarsgame.game.SkywarsGame;
import com.deyo.hypixel.type.skywarsgame.game.SkywarsGameStatus;
import com.deyo.hypixel.type.skywarsgame.user.SkywarsPlayer;

@CommandParameters(
        description = "Force starts the skywars game",
        usage = "/forcestart [seconds]",
        permission = Rank.STAFF,
        allowsConsole = false,
        labels = "forcestart"
)
public class ForceStartCommand extends HypixelCommand {

    @Override
    public void registerUsage(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof SkywarsPlayer player)) return;

            SkywarsGame game = TypeSkywarsGameLoader.getPlayerGame(player);
            if (game == null) {
                player.sendMessage("<c>You are not in a game!");
                return;
            }

            if (game.getGameStatus() != SkywarsGameStatus.WAITING &&
                    game.getGameStatus() != SkywarsGameStatus.STARTING) {
                player.sendMessage("<c>The game has already started!");
                return;
            }

            game.forceStart(5);
            player.sendMessage("<a>Force starting the game in 5 seconds!");
        });

        var secondsArg = ArgumentType.Integer("seconds").min(1).max(60);
        command.addSyntax((sender, context) -> {
            if (!permissionCheck(sender)) return;
            if (!(sender instanceof SkywarsPlayer player)) return;

            SkywarsGame game = TypeSkywarsGameLoader.getPlayerGame(player);
            if (game == null) {
                player.sendMessage("<c>You are not in a game!");
                return;
            }

            if (game.getGameStatus() != SkywarsGameStatus.WAITING &&
                    game.getGameStatus() != SkywarsGameStatus.STARTING) {
                player.sendMessage("<c>The game has already started!");
                return;
            }

            int seconds = context.get(secondsArg);
            game.forceStart(seconds);
            player.sendMessage("<a>Force starting the game in {} seconds!", seconds);
        }, secondsArg);
    }
}
