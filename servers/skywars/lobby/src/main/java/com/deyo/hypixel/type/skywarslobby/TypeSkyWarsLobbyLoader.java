package com.deyo.hypixel.type.skywarslobby;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.timer.TaskSchedule;
import com.deyo.hypixel.commons.CustomWorlds;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.HypixelGenericLoader;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.data.GameDataHandler;
import com.deyo.hypixel.type.generic.data.handlers.SkywarsDataHandler;
import com.deyo.hypixel.type.generic.entity.hologram.PlayerHolograms;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.tab.TablistManager;
import com.deyo.hypixel.type.generic.tab.TablistModule;
import com.deyo.hypixel.type.lobby.LobbyTypeLoader;
import com.deyo.hypixel.type.lobby.events.LobbyAFKEvents;
import com.deyo.hypixel.type.lobby.events.LobbyItemEvents;
import com.deyo.hypixel.type.lobby.events.LobbyLaunchPadEvents;
import com.deyo.hypixel.type.lobby.events.LobbyPlayerJoinEvents;
import com.deyo.hypixel.type.lobby.events.LobbyPlayerMove;
import com.deyo.hypixel.type.lobby.events.LobbyPlayerSpawnEvents;
import com.deyo.hypixel.type.lobby.events.LobbyWorldEvent;
import com.deyo.hypixel.type.lobby.item.LobbyItem;
import com.deyo.hypixel.type.lobby.item.LobbyItemHandler;
import com.deyo.hypixel.type.lobby.item.impl.HidePlayers;
import com.deyo.hypixel.type.lobby.item.impl.LobbySelector;
import com.deyo.hypixel.type.lobby.item.impl.PlayCompass;
import com.deyo.hypixel.type.lobby.item.impl.ProfileItem;
import com.deyo.hypixel.type.lobby.launchpad.LaunchPad;
import com.deyo.hypixel.type.lobby.parkour.LobbyParkourManager;
import com.deyo.hypixel.type.lobby.parkour.Parkour;
import com.deyo.hypixel.type.skywarslobby.hologram.LeaderboardHologramManager;
import com.deyo.hypixel.type.skywarslobby.hologram.SoulWellHologramManager;
import com.deyo.hypixel.type.skywarslobby.item.SkywarsMenuItem;
import com.deyo.hypixel.type.skywarslobby.kit.SkywarsKitRegistry;
import com.deyo.hypixel.type.skywarslobby.level.SkywarsLevelRegistry;
import com.deyo.hypixel.type.skywarslobby.parkour.SkywarsLobbyParkour;
import com.deyo.hypixel.type.skywarslobby.perk.SkywarsPerkRegistry;
import com.deyo.hypixel.type.skywarslobby.soulwell.SoulWellParticleManager;
import com.deyo.hypixel.type.skywarslobby.soulwell.SoulWellUpgradeRegistry;
import com.deyo.hypixel.type.skywarslobby.util.SkyWarsLobbyMap;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TypeSkyWarsLobbyLoader implements LobbyTypeLoader {
    @Getter
    private final LobbyItemHandler itemHandler = new LobbyItemHandler();
    @Getter
    private static LeaderboardHologramManager leaderboardManager;
    private LobbyParkourManager parkourManager;
    private final Pos spawnPont = new Pos(-3.5, 66, 0.5, -90, 0);

    @Override
    public ServerType getType() {
        return ServerType.SKYWARS_LOBBY;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        // Initialize registries
        SkywarsKitRegistry.initialize();
        SkywarsPerkRegistry.initialize();
        SkywarsLevelRegistry.initialize();
        SoulWellUpgradeRegistry.initialize();
        Logger.info("Initialized SkyWars kit, perk, level, and soul well upgrade registries");

        SkyWarsLobbyScoreboard.start();

        // Place map item frames in the lobby
        SkyWarsLobbyMap skywarsLobbyMap = new SkyWarsLobbyMap();
        skywarsLobbyMap.placeItemFrames(HypixelConst.getInstanceContainer());

        // Initialize parkour manager
        parkourManager = new LobbyParkourManager(getParkour());

        // Initialize leaderboard holograms
        LeaderboardHologramManager.initialize(HypixelConst.getInstanceContainer());

        // Initialize Soul Well hologram
        SoulWellHologramManager.initialize(HypixelConst.getInstanceContainer());

        // Initialize Soul Well particles
        SoulWellParticleManager.initialize(HypixelConst.getInstanceContainer());

        // Register all hotbar items
        getHotbarItems().values().forEach(itemHandler::add);

        // Register commands
        HypixelGenericLoader.loopThroughPackage("com.deyo.hypixel.type.skywarslobby.commands", HypixelCommand.class).forEach(command -> {
            try {
                MinecraftServer.getCommandManager().register(command.getCommand());
            } catch (Exception e) {
                Logger.error(e, "Failed to register command " + command.getCommand().getName() + " in class " + command.getClass().getSimpleName());
            }
        });

        // Schedule hologram updates every 2 seconds
        MinecraftServer.getSchedulerManager().buildTask(PlayerHolograms::updateExternalHolograms)
                .delay(TaskSchedule.seconds(5))
                .repeat(TaskSchedule.seconds(2))
                .schedule();

        LobbyTypeLoader.registerLobbyCommands();
    }

    @Override
    public List<LaunchPad> getLaunchPads() {
        return List.of();
    }

    @Override
    public Map<Integer, LobbyItem> getHotbarItems() {
        return Map.of(
                0, new PlayCompass(),
                1, new ProfileItem(),
                2, new SkywarsMenuItem(),
                7, new HidePlayers(),
                8, new LobbySelector()
        );
    }

    @Override
    public Parkour getParkour() {
        return new SkywarsLobbyParkour();
    }

    @Override
    public LobbyParkourManager getParkourManager() {
        return parkourManager;
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
                return List.of();
            }
        };
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                (type) -> spawnPont,
                false
        );
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        List<HypixelEventClass> events = new ArrayList<>(HypixelGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.skywarslobby.events",
                HypixelEventClass.class
        ).toList());
        // Add lobby base events
        events.add(new LobbyAFKEvents());
        events.add(new LobbyItemEvents());
        events.add(new LobbyLaunchPadEvents());
        events.add(new LobbyPlayerJoinEvents());
        events.add(new LobbyPlayerSpawnEvents());
        events.add(new LobbyWorldEvent());
        events.add(new LobbyPlayerMove(spawnPont));
        return events;
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return HypixelGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.skywarslobby.events.custom",
                HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return HypixelGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.skywarslobby.npcs",
                HypixelNPC.class
        ).toList();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<RedisMessageHandler<?, ?>> getServiceHandlers() {
        return (List) HypixelGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.skywarslobby.redis.service",
                RedisMessageHandler.class
        ).toList();
    }

    @Override
    public List<RedisMessageHandler<?, ?>> getProxyHandlers() {
        return List.of();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.SKYWARS_LOBBY;
    }

    @Override
    public List<Class<? extends GameDataHandler>> getAdditionalDataHandlers() {
        return List.of(SkywarsDataHandler.class);
    }
}
