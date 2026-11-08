package com.deyo.hypixel.type.lobby.commands;

import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.lobby.LobbyTypeLoader;
import com.deyo.hypixel.type.lobby.parkour.LobbyParkourManager;
import com.deyo.hypixel.type.lobby.parkour.Parkour;

@CommandParameters(labels = "parkour", allowsConsole = false, description = "Parkour related commands", permission = Rank.DEFAULT, usage = "/parkour")
public class ParkourCommand extends HypixelCommand {

	@Override
	public void registerUsage(MinestomCommand command) {
		LobbyParkourManager manager;
		if (HypixelConst.getTypeLoader() instanceof LobbyTypeLoader loader) {
			manager = loader.getParkourManager();
		} else {
			manager = null;
		}

		command.setDefaultExecutor(((sender, context) -> {
			sender.sendMessage("<c>/parkour start");
			sender.sendMessage("<c>/parkour reset");
			sender.sendMessage("<c>/parkour checkpoint");
			sender.sendMessage("<c>/parkour cancel");
		}));

		// start
		command.addSyntax((sender, context) -> {
			if (manager == null) {
				sender.sendMessage("<c>This command is unavailable here.");
				return;
			}
			if (!(sender instanceof HypixelPlayer player)) return;
			if (manager.getPerPlayerStartTime().containsKey(player.getUuid())) {
				sender.sendMessage("<c>You are currently in a parkour race. Use /parkour reset");
				return;
			}

			player.teleport(manager.getParkour().getStartLocation());
			manager.startParkour(player);
		}, new ArgumentLiteral("start"));

		// cancel
		command.addSyntax((sender, context) -> {
			if (manager == null) {
				sender.sendMessage("<c>This command is unavailable here.");
				return;
			}
			if (!(sender instanceof HypixelPlayer player)) return;
			manager.cancelParkour(player);
		}, new ArgumentLiteral("cancel"));

		// reset
		command.addSyntax((sender, context) -> {
			if (manager == null) {
				sender.sendMessage("<c>This command is unavailable here.");
				return;
			}
			if (!(sender instanceof HypixelPlayer player)) return;
			manager.resetPlayer(player);
		}, new ArgumentLiteral("reset"));

		// checkpoint
		command.addSyntax((sender, context) -> {
			if (manager == null) {
				sender.sendMessage("<c>This command is unavailable here.");
				return;
			}
			if (!(sender instanceof HypixelPlayer player)) return;
			Parkour parkour = manager.getParkour();
			if (!manager.getPerPlayerStartTime().containsKey(player.getUuid())) {
				sender.sendMessage("<c>You haven't started the parkour challenge yet! Use <e>/parkour start</e> to start!");
				return;
			}
			int checkpointIndex = manager.getPerPlayerStartTime().get(player.getUuid()).lastCheckpointIndex();
			if (checkpointIndex == 0) {
				player.teleport(parkour.getStartLocation());
			} else {
				player.teleport(parkour.getCheckpoints().get(checkpointIndex - 1).asPos());
			}
		}, new ArgumentLiteral("checkpoint"));
	}

}
