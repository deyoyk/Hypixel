package com.deyo.hypixel.type.ravengarddungeon;

import com.google.gson.Gson;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import com.deyo.hypixel.commons.CustomWorlds;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.Tuple;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.type.generic.HypixelTypeLoader;
import com.deyo.hypixel.type.generic.RavengardTypeLoader;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.tab.TablistManager;
import com.deyo.hypixel.type.generic.tab.TablistModule;
import com.deyo.hypixel.type.generic.world.HypixelWorldLoader;
import com.deyo.hypixel.type.ravengarddungeon.config.RavengardDungeonConfig;
import com.deyo.hypixel.type.ravengarddungeon.game.RavengardDungeonGame;
import com.deyo.hypixel.type.ravengarddungeon.user.RavengardDungeonPlayer;
import com.deyo.hypixel.type.ravengardgeneric.RavengardGenericLoader;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class TypeRavengardDungeonLoader implements RavengardTypeLoader {
    private static final Path CONFIGURATION = Path.of("./configuration/ravengard/dungeon_1.json");

    @Getter
    private static RavengardDungeonGame game;
    private RavengardDungeonConfig config;

    @Override
    public ServerType getType() {
        return ServerType.RAVENGARD_DUNGEON;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        try {
            config = new Gson().fromJson(Files.readString(CONFIGURATION), RavengardDungeonConfig.class);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load " + CONFIGURATION, exception);
        }
        Path polarPath = CONFIGURATION.getParent().resolve(config.polar());
        if (!Files.isRegularFile(polarPath)) {
            throw new IllegalStateException("Ravengard dungeon map not found: " + polarPath);
        }
        InstanceContainer temp = MinecraftServer.getInstanceManager().createInstanceContainer();
        final Instance instance;
        try {
            instance = HypixelWorldLoader.loadFrom(polarPath, temp, MinecraftServer.getInstanceManager());
        } catch (IOException e) {
            throw new IllegalStateException("Could not load Ravengard dungeon map: " + polarPath);
        }
        game = new RavengardDungeonGame(instance, config);
        MinecraftServer.getConnectionManager().setPlayerProvider((connection, profile) ->
                new RavengardDungeonPlayer(connection, profile));
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        // same recipe as skyblock islands: a fullbright dimension renders on the
        // client without any light data, which runtime-built instances lack
        MinecraftServer.getDimensionTypeRegistry().register(
                net.kyori.adventure.key.Key.key("ravengard:dungeon"),
                net.minestom.server.world.DimensionType.builder()
                        .ambientLight(1)
                        .build());
        com.deyo.hypixel.type.ravengarddungeon.game.DungeonInstanceRegistry.startExpiryTask();
        com.deyo.hypixel.type.ravengarddungeon.game.DungeonMinimapIcons.register();
        com.deyo.hypixel.type.ravengarddungeon.interactables.InteractableRegistry.startTask();
        com.deyo.hypixel.type.ravengardgeneric.commands.SpawnSatchelCommand.spawner = player ->
                com.deyo.hypixel.type.ravengarddungeon.interactables.DungeonSatchel.spawn(
                        player.getInstance(), player.getPosition());
        com.deyo.hypixel.type.ravengardgeneric.commands.DungeonCommand.localJoin = (player, instanceId) -> {
            var instance = com.deyo.hypixel.type.ravengarddungeon.game.DungeonInstanceRegistry.findByIdPrefix(instanceId);
            if (instance == null) {
                return false;
            }
            com.deyo.hypixel.type.ravengarddungeon.game.DungeonInstanceRegistry.sendPlayerIn(player, instance);
            return true;
        };
        startOrchestratorHeartbeat();
    }

    private void startOrchestratorHeartbeat() {
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            java.util.List<com.deyo.hypixel.type.game.game.GameObject> games = new java.util.ArrayList<>();
            for (var instance : com.deyo.hypixel.type.ravengarddungeon.game.DungeonInstanceRegistry.all()) {
                com.deyo.hypixel.type.game.game.GameObject game = new com.deyo.hypixel.type.game.game.GameObject();
                game.setGameId(instance.getGameId());
                game.setType(ServerType.RAVENGARD_DUNGEON);
                game.setMap(instance.getPlayers().isEmpty()
                        ? "empty:" + instance.getRemainingLifeSeconds() : "generated");
                game.setGameTypeName(instance.getMode());
                game.setAcceptingJoins(instance.isAcceptingJoins());
                game.setInvolvedPlayers(new java.util.ArrayList<>(instance.getPlayers()));
                game.setDisconnectedPlayers(java.util.List.of());
                games.add(game);
            }
            var heartbeat = new com.deyo.hypixel.commons.protocol.objects.orchestrator.GameHeartbeatProtocol.HeartbeatMessage(
                    com.deyo.hypixel.type.generic.HypixelConst.getServerUUID(),
                    com.deyo.hypixel.type.generic.HypixelConst.getShortenedServerName(),
                    getType(),
                    com.deyo.hypixel.type.generic.HypixelConst.getMaxPlayers(),
                    MinecraftServer.getConnectionManager().getOnlinePlayers().size(),
                    games,
                    java.util.List.of(),
                    com.deyo.hypixel.type.ravengarddungeon.game.DungeonInstanceRegistry.remainingSlots());
            new com.deyo.hypixel.proxyapi.ProxyService(ServiceType.ORCHESTRATOR).handleRequest(heartbeat);
        }).delay(net.minestom.server.timer.TaskSchedule.seconds(5))
                .repeat(net.minestom.server.timer.TaskSchedule.seconds(1)).schedule();
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
                return List.of();
            }
        };
    }

    @Override
    public HypixelTypeLoader.LoaderValues getLoaderValues() {
        Pos spawn = config == null ? new Pos(0.5, 65, 0.5) : config.spawnPosition();
        return new HypixelTypeLoader.LoaderValues(spawn, false);
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        return RavengardGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.ravengarddungeon.events",
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
    public List<RedisMessageHandler<?, ?>> getServiceHandlers() {
        return List.of(new com.deyo.hypixel.type.ravengarddungeon.redis.DungeonInstantiateGameHandler());
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return null;
    }

    @Override
    public boolean headerFooterPerPlayer() {
        return true;
    }

    @Override
    public @NonNull Optional<Tuple<Text, Text>> headerFooter() {
        return Optional.of(new Tuple<>(Text.empty(), Text.empty()));
    }
}
