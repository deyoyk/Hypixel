package com.deyo.hypixel.type.skywarsconfigurator;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import com.deyo.hypixel.commons.CustomWorlds;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.type.generic.HypixelGenericLoader;
import com.deyo.hypixel.type.generic.HypixelTypeLoader;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.redis.RedisOriginServer;
import com.deyo.hypixel.type.generic.tab.EmptyTabModule;
import com.deyo.hypixel.type.generic.tab.TablistManager;
import com.deyo.hypixel.type.generic.tab.TablistModule;
import com.deyo.hypixel.type.generic.user.HypixelPlayer;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.List;
import java.util.UUID;

public class TypeSkyWarsConfiguratorLoader implements HypixelTypeLoader {

    @Setter
    @Getter
    private static MapConfigurationSession currentSession;

    @Override
    public ServerType getType() {
        return ServerType.SKYWARS_CONFIGURATOR;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        MinecraftServer.getConnectionManager().setPlayerProvider((PlayerConnection playerConnection, GameProfile gameProfile) -> {
            HypixelPlayer player = new HypixelPlayer(playerConnection, gameProfile);

            UUID uuid = gameProfile.uuid();
            String username = gameProfile.name();

            ServerType originServer = RedisOriginServer.consume(uuid);

            if (originServer != null) {

                player.setOriginServer(originServer);
            }

            // Set player to creative mode for configuration
            player.setGameMode(GameMode.CREATIVE);

            Logger.info("Configurator: " + username + " joined");

            return player;
        });
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        HypixelGenericLoader.loopThroughPackage("com.deyo.hypixel.type.skywarsconfigurator.commands", HypixelCommand.class).forEach(command -> {
            try {
                MinecraftServer.getCommandManager().register(command.getCommand());
            } catch (Exception e) {
                Logger.error(e, "Failed to register command " + command.getCommand().getName());
            }
        });

        Logger.info("SkyWars Configurator loaded. Use /swconfig to start.");
    }

    @Override
    public List<ServiceType> getRequiredServices() {
        return List.of();
    }

    @Override
    public TablistManager getTablistManager() {
        return new TablistManager() {
            @Override
            public List<TablistModule> getModules() {
                return List.of(new EmptyTabModule());
            }
        };
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                (_) -> new Pos(0, 100, 0, 0, 0),
                false
        );
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        return HypixelGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.skywarsconfigurator.events",
                HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return List.of();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return List.of();
    }


    @Override
    public List<RedisMessageHandler<?, ?>> getProxyHandlers() {
        return List.of();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return null;
    }
}
