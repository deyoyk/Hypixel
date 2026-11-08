package com.deyo.hypixel.type.prototypelobby;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.commons.CustomWorlds;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.HypixelGenericLoader;
import com.deyo.hypixel.type.generic.data.GameDataHandler;
import com.deyo.hypixel.type.generic.data.handlers.PrototypeLobbyDataHandler;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.tab.EmptyTabModule;
import com.deyo.hypixel.type.generic.tab.TablistManager;
import com.deyo.hypixel.type.generic.tab.TablistModule;
import com.deyo.hypixel.type.lobby.LobbyTypeLoader;
import com.deyo.hypixel.type.lobby.events.LobbyAFKEvents;
import com.deyo.hypixel.type.lobby.events.LobbyItemEvents;
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
import com.deyo.hypixel.type.lobby.parkour.LobbyParkourManager;
import com.deyo.hypixel.type.lobby.parkour.Parkour;
import com.deyo.hypixel.type.prototypelobby.parkour.PrototypeLobbyParkour;
import com.deyo.hypixel.type.prototypelobby.util.PrototypeLobbyMap;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TypePrototypeLobbyLoader implements LobbyTypeLoader {
    private static final LobbyItemHandler itemHandler = new LobbyItemHandler();
    public static LobbyParkourManager parkourManager;
    private final Pos SPAWN_POS = new Pos(11.5, 75, 0.5, 90, 0);

    @Override
    public ServerType getType() {
        return ServerType.PROTOTYPE_LOBBY;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        PrototypeLobbyScoreboard.start();

        PrototypeLobbyMap prototypeLobbyMap = new PrototypeLobbyMap();
        prototypeLobbyMap.placeItemFrames(HypixelConst.getInstanceContainer());

        // Register all hotbar items
        getHotbarItems().values().forEach(itemHandler::add);

        parkourManager = new LobbyParkourManager(getParkour());

        LobbyTypeLoader.registerLobbyCommands();
    }

    @Override
    public Parkour getParkour() {
        return new PrototypeLobbyParkour();
    }

    @Override
    public @Nullable LobbyParkourManager getParkourManager() {
        return parkourManager;
    }

    @Override
    public LobbyItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public List<LaunchPad> getLaunchPads() {
        return List.of(); // No launch pads for prototype lobby
    }

    @Override
    public Map<Integer, LobbyItem> getHotbarItems() {
        return Map.of(
                0, new PlayCompass(),
                1, new ProfileItem(),
            4, new Collectibles(),
                7, new HidePlayers(),
                8, new LobbySelector()
        );
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
            (type) -> SPAWN_POS,
                false
        );
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        List<HypixelEventClass> events = new ArrayList<>(HypixelGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.prototypelobby.events",
                HypixelEventClass.class
        ).toList());
        // Add lobby base events
        events.add(new LobbyAFKEvents());
        events.add(new LobbyItemEvents());
        events.add(new LobbyPlayerJoinEvents());
        events.add(new LobbyPlayerSpawnEvents());
        events.add(new LobbyParkourEvents());
        events.add(new LobbyWorldEvent());
        events.add(new LobbyPlayerMove(SPAWN_POS));
        return events;
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return HypixelGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.prototypelobby.events.custom",
                HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return HypixelGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.prototypelobby.npcs",
                HypixelNPC.class
        ).toList();
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<RedisMessageHandler<?, ?>> getServiceHandlers() {
        return (List) HypixelGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.prototypelobby.redis.service",
                RedisMessageHandler.class
        ).toList();
    }

    @Override
    public List<RedisMessageHandler<?, ?>> getProxyHandlers() {
        return List.of();
    }

    @Override
    public List<Class<? extends GameDataHandler>> getAdditionalDataHandlers() {
        return List.of(PrototypeLobbyDataHandler.class);
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.PROTOTYPE_LOBBY;
    }
}
