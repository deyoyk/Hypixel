package com.deyo.hypixel.type.island;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.commons.CustomWorlds;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.type.generic.HypixelTypeLoader;
import com.deyo.hypixel.type.generic.SkyBlockTypeLoader;

import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;

import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.tab.TablistManager;
import com.deyo.hypixel.type.generic.tab.TablistModule;
import com.deyo.hypixel.type.island.lifecycle.IslandLifecycleSteps;
import com.deyo.hypixel.type.island.tab.IslandGuestsModule;
import com.deyo.hypixel.type.island.tab.IslandMemberModule;
import com.deyo.hypixel.type.island.tab.IslandServerModule;
import com.deyo.hypixel.type.skyblockgeneric.SkyBlockGenericLoader;
import com.deyo.hypixel.type.skyblockgeneric.minion.MinionHandler;
import com.deyo.hypixel.type.skyblockgeneric.tabmodules.AccountInformationModule;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypeIslandLoader implements SkyBlockTypeLoader {
    public static final Pos SPAWN_POS = new Pos(7.5, 100, 7.5);

    @Override
    public ServerType getType() {
        return ServerType.SKYBLOCK_ISLAND;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        Logger.info("TypeIslandLoader initialized!");

        IslandLifecycleSteps.register();
        new MinionHandler(MinecraftServer.getSchedulerManager()).start();
    }

    @Override
    public void afterInitialize(MinecraftServer server) {

    }

    @Override
    public HypixelTypeLoader.LoaderValues getLoaderValues() {
        return new HypixelTypeLoader.LoaderValues(
            (_) -> SPAWN_POS, // Spawn position
                true // Announce death messages
        );
    }

    @Override
    public TablistManager getTablistManager() {
        return new TablistManager() {
            @Override
            public List<TablistModule> getModules() {
                return new ArrayList<>(List.of(
                        new IslandMemberModule(),
                        new IslandGuestsModule(),
                        new IslandServerModule(),
                        new AccountInformationModule()
                ));
            }
        };
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.island.events.traditional",
                HypixelEventClass.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return List.of();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return new ArrayList<>();
    }


    @Override
    public List<RedisMessageHandler<?, ?>> getProxyHandlers() {
        return List.of();
    }

    @Override
    public List<ServiceType> getRequiredServices() {
        return List.of(ServiceType.AUCTION_HOUSE, ServiceType.ITEM_TRACKER);
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return null;
    }
}
