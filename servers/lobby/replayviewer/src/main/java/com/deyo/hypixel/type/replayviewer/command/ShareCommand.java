package com.deyo.hypixel.type.replayviewer.command;

import com.deyo.hypixel.type.generic.command.CommandParameters;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import com.deyo.hypixel.type.generic.user.categories.Rank;
import com.deyo.hypixel.type.replayviewer.TypeReplayViewerLoader;
import com.deyo.hypixel.type.replayviewer.playback.ReplaySession;
import com.deyo.hypixel.type.replayviewer.util.ReplayShareUtil;

@CommandParameters(
	description = "Share your current replay position",
	usage = "/share",
	permission = Rank.DEFAULT,
	allowsConsole = false,
	labels = "share"
)
public class ShareCommand extends HypixelCommand {

	@Override
	public void registerUsage(MinestomCommand command) {
		command.setDefaultExecutor((sender, _) -> {
			HypixelPlayer player = (HypixelPlayer) sender;

			var sessionOpt = TypeReplayViewerLoader.getSession(player.getUuid());
			if (sessionOpt.isEmpty()) {
				player.sendMessage("<c>You are not watching a replay!");
				return;
			}

			ReplaySession session = sessionOpt.get();
			ReplayShareUtil.sendShareCommandMessage(player, session);
		});
	}
}

