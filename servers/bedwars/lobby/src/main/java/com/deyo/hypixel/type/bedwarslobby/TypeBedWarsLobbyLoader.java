package com.deyo.hypixel.type.bedwarslobby;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.timer.TaskSchedule;
import com.deyo.hypixel.commons.CustomWorlds;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.type.bedwarslobby.hologram.LeaderboardHologramManager;
import com.deyo.hypixel.type.bedwarslobby.item.impl.BedWarsMenu;
import com.deyo.hypixel.type.bedwarslobby.launchpad.BedWarsLaunchPads;
import com.deyo.hypixel.type.bedwarslobby.parkour.BedWarsLobbyParkour;
import com.deyo.hypixel.type.bedwarslobby.util.BedWarsLobbyMap;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.HypixelGenericLoader;
import com.deyo.hypixel.type.generic.collectibles.bedwars.BedWarsCollectibleCatalog;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.data.GameDataHandler;
import com.deyo.hypixel.type.generic.data.handlers.BedWarsDataHandler;
import com.deyo.hypixel.type.generic.entity.hologram.PlayerHolograms;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.leaderboard.BedWarsLeaderboardAggregator;
import com.deyo.hypixel.type.generic.tab.EmptyTabModule;
import com.deyo.hypixel.type.generic.tab.TablistManager;
import com.deyo.hypixel.type.generic.tab.TablistModule;
import com.deyo.hypixel.type.lobby.LobbyTypeLoader;
import com.deyo.hypixel.type.lobby.events.LobbyAFKEvents;
import com.deyo.hypixel.type.lobby.events.LobbyItemEvents;
import com.deyo.hypixel.type.lobby.events.LobbyLaunchPadEvents;
import com.deyo.hypixel.type.lobby.events.LobbyParkourEvents;
import com.deyo.hypixel.type.lobby.events.LobbyPlayerJoinEvents;
import com.deyo.hypixel.type.lobby.events.LobbyPlayerMove;
import com.deyo.hypixel.type.lobby.events.LobbyPlayerSpawnEvents;
import com.deyo.hypixel.type.lobby.events.LobbyWorldEvent;
import com.deyo.hypixel.type.lobby.item.LobbyItem;
import com.deyo.hypixel.type.lobby.item.LobbyItemHandler;
import com.deyo.hypixel.type.lobby.item.impl.Collectibles;
import com.deyo.hypixel.type.lobby.item.impl.HidePlayers;
import com.deyo.hypixel.type.lobby.item.impl.LobbySelector;
import com.deyo.hypixel.type.lobby.item.impl.PlayCompass;
import com.deyo.hypixel.type.lobby.item.impl.ProfileItem;
import com.deyo.hypixel.type.lobby.launchpad.LaunchPad;
import com.deyo.hypixel.type.lobby.launchpad.LaunchPadHandler;
import com.deyo.hypixel.type.lobby.parkour.LobbyParkourManager;
import com.deyo.hypixel.type.lobby.parkour.Parkour;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TypeBedWarsLobbyLoader implements LobbyTypeLoader {
    public static BedWarsLobbyMap bedWarsLobbyMap = new BedWarsLobbyMap();
    public static LobbyParkourManager parkourManager;
    private final Pos spawnPoint = new Pos(-39.5, 72, 0, -90, 0);

    @Getter
    private final LobbyItemHandler itemHandler = new LobbyItemHandler();

    @Override
    public ServerType getType() {
        return ServerType.BEDWARS_LOBBY;
    }

    @Override
    public Parkour getParkour() {
        return new BedWarsLobbyParkour();
    }

    @Override
    public void onInitialize(MinecraftServer server) {
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        BedWarsCollectibleCatalog.initialize();
        BedWarsLobbyScoreboard.start();
        bedWarsLobbyMap.placeItemFrames(HypixelConst.getInstanceContainer());

        // Register launch pads
        LaunchPadHandler.register(MinecraftServer.getSchedulerManager(), getLaunchPads());

        // Register all hotbar items
        getHotbarItems().values().forEach(itemHandler::add);

        // Initialize leaderboard holograms
        LeaderboardHologramManager.initialize(HypixelConst.getInstanceContainer());

        // Register commands
        HypixelGenericLoader.loopThroughPackage("com.deyo.hypixel.type.bedwarslobby.commands", HypixelCommand.class).forEach(command -> {
            try {
                MinecraftServer.getCommandManager().register(command.getCommand());
            } catch (Exception e) {
                Logger.error(e, "Failed to register command " + command.getCommand().getName() + " in class " + command.getClass().getSimpleName());
            }
        });

        // Start the leaderboard aggregator (for daily/weekly/monthly leaderboard updates)
        BedWarsLeaderboardAggregator.initialize();

        // Schedule hologram updates every 2 seconds
        MinecraftServer.getSchedulerManager().buildTask(PlayerHolograms::updateExternalHolograms).delay(TaskSchedule.seconds(5))
          .repeat(TaskSchedule.seconds(2))
          .schedule();

        parkourManager = new LobbyParkourManager(getParkour());

        LobbyTypeLoader.registerLobbyCommands();
    }

    @Override
    public List<LaunchPad> getLaunchPads() {
        return Arrays.asList(BedWarsLaunchPads.values());
    }

    @Override
    public @Nullable LobbyParkourManager getParkourManager() {
        return parkourManager;
    }

    @Override
    public Map<Integer, LobbyItem> getHotbarItems() {
        return Map.of(
                0, new PlayCompass(),
                1, new ProfileItem(),
                2, new BedWarsMenu(),
                4, new Collectibles(),
                7, new HidePlayers(),
                8, new LobbySelector()
        );
    }

    @Override
    public List<ServiceType> getRequiredServices() {
        return List.of(ServiceType.ORCHESTRATOR);
    }

    @Override
    public TablistManager getTablistManager() {
        return new TablistManager() {
            @Override
            public List<TablistModule> getModules() {
                return List.of(
                    new EmptyTabModule(),
                    new EmptyTabModule(),
                    new EmptyTabModule(),
                    new EmptyTabModule()
                );
            }
        };
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                (type) -> spawnPoint,
                false
        );
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        List<HypixelEventClass> events = new ArrayList<>(HypixelGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.bedwarslobby.events",
                HypixelEventClass.class
        ).toList());
        // Add lobby base events
        events.add(new LobbyAFKEvents());
        events.add(new LobbyItemEvents());
        events.add(new LobbyParkourEvents());
        events.add(new LobbyLaunchPadEvents());
        events.add(new LobbyPlayerJoinEvents());
        events.add(new LobbyPlayerSpawnEvents());
        events.add(new LobbyWorldEvent());
        events.add(new LobbyPlayerMove(spawnPoint));
        return events;
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return HypixelGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.bedwarslobby.events.custom",
                HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return HypixelGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.bedwarslobby.npcs",
                HypixelNPC.class
        ).toList();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<RedisMessageHandler<?, ?>> getServiceHandlers() {
        return (List) HypixelGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.bedwarslobby.redis.service",
                RedisMessageHandler.class
        ).toList();
    }

    @Override
    public List<RedisMessageHandler<?, ?>> getProxyHandlers() {
        return List.of();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.BEDWARS_LOBBY;
    }

    @Override
    public List<Class<? extends GameDataHandler>> getAdditionalDataHandlers() {
        return List.of(BedWarsDataHandler.class);
    }
}
