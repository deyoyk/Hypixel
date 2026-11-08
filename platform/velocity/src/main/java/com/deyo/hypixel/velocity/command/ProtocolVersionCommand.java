package com.deyo.hypixel.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.Player;
import com.deyo.hypixel.commons.text.Text;

import java.util.List;

public class ProtocolVersionCommand implements SimpleCommand {

	@Override
	public void execute(Invocation invocation) {
		CommandSource source = invocation.source();
		if (!(source instanceof Player player)) {
			return;
		}
		ProtocolVersion version = player.getProtocolVersion();
		List<String> versionsSupportedBy = version.getVersionsSupportedBy();
		player.sendMessage(Text.of("{} <7>({})", version.getProtocol(), String.join(", ", versionsSupportedBy)));
	}
}
