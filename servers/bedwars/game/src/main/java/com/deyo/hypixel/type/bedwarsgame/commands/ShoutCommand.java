package com.deyo.hypixel.type.bedwarsgame.commands;

import net.minestom.server.command.builder.arguments.ArgumentType;
import com.deyo.hypixel.commons.bedwars.BedWarsGameType;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.bedwarsgame.game.v2.BedWarsGame;
import com.deyo.hypixel.type.bedwarsgame.user.BedWarsPlayer;
import com.deyo.hypixel.type.game.game.GameState;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.categories.Rank;

@CommandParameters(labels = "shout",
		description = "Shouts a message to all players on the game.",
		usage = "/shout <message>",
		permission = Rank.DEFAULT,
		allowsConsole = false)
public class ShoutCommand extends HypixelCommand {

	@Override
	public void registerUsage(MinestomCommand command) {
		var messageArg = ArgumentType.String("message");

		command.addSyntax((sender, context) -> {
			final String message = context.get("message");
			if (!permissionCheck(sender)) return;

			BedWarsPlayer player = (BedWarsPlayer) sender;
			BedWarsGame game = player.getGame();
			if (game == null) {
				player.sendMessage("<c>You are not in a game.");
				return;
			}
			if (game.getGameType() == BedWarsGameType.ONE_EIGHT) {
				player.sendMessage("<c>This command is unavailable.");
				return;
			}
			if (game.getState() != GameState.IN_PROGRESS) {
				player.sendMessage("<c>You can only shout messages during an active game.");
				return;
			}
			if (message.isEmpty()) {
				player.sendMessage("<c>Please provide a message to shout.");
				return;
			}
			for (BedWarsPlayer receiver : game.getPlayers()) {
				receiver.sendMessage("<6>[SHOUT] <r>{}<r>: {}", player.getFullDisplayName(), message);
			}

			if (game.getReplayManager().isRecording()) {
				game.getReplayManager().recordPlayerChat(player, message, true);
			}
		}, messageArg);

		command.setDefaultExecutor((sender, _) -> {
			sender.sendMessage("<c>Please provide a message to shout.");
		});
	}

}
