package com.deyo.hypixel.type.ravengardgeneric;

import lombok.Getter;
import lombok.SneakyThrows;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import com.deyo.hypixel.commons.CustomWorlds;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.HypixelTypeLoader;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.event.HypixelEventHandler;
import com.deyo.hypixel.type.generic.packet.HypixelPacketClientListener;
import com.deyo.hypixel.type.generic.packet.HypixelPacketServerListener;
import com.deyo.hypixel.type.generic.redis.RedisOriginServer;
import com.deyo.hypixel.type.generic.resourcepack.ResourcePackManager;
import com.deyo.hypixel.type.generic.world.HypixelWorldLoader;
import com.deyo.hypixel.type.ravengardgeneric.resourcepack.RavengardPack;
import com.deyo.hypixel.type.ravengardgeneric.user.RavengardPlayer;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;
import org.tinylog.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public record RavengardGenericLoader(HypixelTypeLoader typeLoader) {

    @Getter
    private static MinecraftServer server;

    @Nullable
    public static Instance tutorialInstance;

    @SneakyThrows
    public void initialize(MinecraftServer server) {
        RavengardGenericLoader.server = server;
        com.deyo.hypixel.type.generic.data.domain.PlayerDataService.register(new com.deyo.hypixel.type.ravengardgeneric.data.RavengardDomain());

        if (typeLoader.getType() == ServerType.RAVENGARD_LOBBY) {
            tutorialInstance = HypixelWorldLoader.load(CustomWorlds.RAVENGARD_TUTORIAL, MinecraftServer.getInstanceManager());
        }

        com.deyo.hypixel.type.ravengardgeneric.item.RavengardItemRegistry.load();
        com.deyo.hypixel.type.ravengardgeneric.shop.RavengardShopRegistry.load();
        com.deyo.hypixel.type.ravengardgeneric.item.attribute.RavengardItemAttribute.registerItemAttributes();
        com.deyo.hypixel.type.ravengardgeneric.entity.mob.RavengardMob.startTicking();
        connectRegions();
        com.deyo.hypixel.type.ravengardgeneric.region.RavengardRegion.cacheRegions();
        com.deyo.hypixel.type.ravengardgeneric.region.RavengardRegionTracker.start();

        loopThroughPackage("com.deyo.hypixel.type.ravengardgeneric.event.actions", HypixelEventClass.class)
                .forEach(HypixelEventHandler::registerEventMethods);

        loopThroughPackage("com.deyo.hypixel.type.ravengardgeneric.commands", HypixelCommand.class)
                .forEach(command -> {
                    try {
                        MinecraftServer.getCommandManager().register(command.getCommand());
                    } catch (Exception e) {
                        Logger.error(e, "Failed to register command {} in class {}",
                                command.getCommand().getName(), command.getClass().getSimpleName());
                    }
                });

        loopThroughPackage("com.deyo.hypixel.type.ravengardgeneric.packets.client", HypixelPacketClientListener.class)
                .forEach(HypixelPacketClientListener::cacheListener);
        loopThroughPackage("com.deyo.hypixel.type.ravengardgeneric.packets.server", HypixelPacketServerListener.class)
                .forEach(HypixelPacketServerListener::cacheListener);

        HypixelPacketClientListener.register(HypixelConst.getEventHandler());
        HypixelPacketServerListener.register(HypixelConst.getEventHandler());
        HypixelEventHandler.register(HypixelConst.getEventHandler());

        RavengardPack testingPack = RavengardPack.fromConfig();
        ResourcePackManager packManager = new ResourcePackManager(testingPack);
        HypixelConst.setResourcePackManager(packManager);
        packManager.initialize();

        MinecraftServer.getConnectionManager().setPlayerProvider((playerConnection, gameProfile) -> {
            RavengardPlayer player = new RavengardPlayer(playerConnection, gameProfile);

            UUID uuid = playerConnection.getPlayer().getUuid();
            String username = playerConnection.getPlayer().getUsername();

            ServerType originServer = RedisOriginServer.consume(uuid);

            if (originServer != null) {

                player.setOriginServer(originServer);
            }

            Logger.info("Received new Ravengard player: {} ({})", username, uuid);
            return player;
        });
    }

    private static void connectRegions() {
        try {
            com.mongodb.ConnectionString connection = new com.mongodb.ConnectionString(
                    com.deyo.hypixel.commons.config.ConfigProvider.settings().getMongodb());
            com.mongodb.MongoClientSettings settings = com.mongodb.MongoClientSettings.builder()
                    .applyConnectionString(connection).build();
            com.mongodb.client.MongoClient client = com.mongodb.client.MongoClients.create(settings);
            com.deyo.hypixel.type.ravengardgeneric.data.monogdb.RavengardRegionDatabase.connect(client);
            com.deyo.hypixel.type.ravengardgeneric.data.monogdb.RavengardProfileDatabase.connect(client);
            com.deyo.hypixel.type.ravengardgeneric.data.monogdb.RavengardTrackedItemsDatabase.connect(client);
        } catch (Exception exception) {
            Logger.warn(exception, "Could not connect the Ravengard region collection; "
                    + "only built-in regions will be available");
        }
    }

    public static List<RavengardPlayer> getLoadedPlayers() {
        List<RavengardPlayer> players = new ArrayList<>();
        MinecraftServer.getConnectionManager().getOnlinePlayers()
                .stream()
                .filter(player -> player instanceof RavengardPlayer)
                .filter(player -> player.getInstance() != null)
                .filter(player -> com.deyo.hypixel.type.generic.data.DataHandler
                        .findUser(player.getUuid()).isPresent())
                .forEach(player -> players.add((RavengardPlayer) player));
        return players;
    }

    public static @Nullable RavengardPlayer getFromUUID(UUID uuid) {
        return getLoadedPlayers().stream()
                .filter(player -> player.getUuid().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public static <T> Stream<T> loopThroughPackage(String packageName, Class<T> clazz) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(clazz);

        return subTypes.stream()
                .map(subClass -> {
                    try {
                        return clazz.cast(subClass.getDeclaredConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                             InvocationTargetException e) {
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull);
    }
}
