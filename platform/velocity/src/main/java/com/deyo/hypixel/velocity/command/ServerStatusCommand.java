package com.deyo.hypixel.velocity.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.deyo.hypixel.velocity.gamemanager.GameManager;
import com.deyo.hypixel.velocity.gamemanager.GameManager.GameServer;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerStatusCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        invocation.source().sendMessage(Text.of("***** Server Status *****"));

        Map<ServerType, ArrayList<GameServer>> serverMap = GameManager.getServers();
        if (serverMap.isEmpty()) {
            invocation.source().sendMessage(Text.of("No servers are currently registered."));
            return;
        }

        for (Map.Entry<ServerType, ArrayList<GameServer>> entry : serverMap.entrySet()) {
            ServerType type = entry.getKey();
            List<GameServer> gameServers = entry.getValue();

            invocation.source().sendMessage(Text.of("{}:", type.name()));

            if (gameServers.isEmpty()) {
                invocation.source().sendMessage(Text.of(" - No servers of this type are currently online."));
                continue;
            }

            for (GameServer server : gameServers) {
                invocation.source().sendMessage(Text.of(" - {} (ID: {}, Port: {}, PlayerCount: {})",
                        server.displayName(),
                        server.internalID(),
                        server.registeredServer().getServerInfo().getAddress().getPort(),
                        server.registeredServer().getPlayersConnected().size()
                ));
            }
        }
    }
}
