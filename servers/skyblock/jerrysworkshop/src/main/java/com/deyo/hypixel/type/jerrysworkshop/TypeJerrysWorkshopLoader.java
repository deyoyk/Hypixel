package com.deyo.hypixel.type.jerrysworkshop;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.commons.CustomWorlds;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.SkyBlockTypeLoader;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.tab.TablistManager;
import com.deyo.hypixel.type.generic.tab.TablistModule;
import com.deyo.hypixel.type.jerrysworkshop.present.Present;
import com.deyo.hypixel.type.skyblockgeneric.SkyBlockGenericLoader;
import com.deyo.hypixel.type.skyblockgeneric.entity.GlassDisplay;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import com.deyo.hypixel.type.skyblockgeneric.tabmodules.AccountInformationModule;
import com.deyo.hypixel.type.skyblockgeneric.tabmodules.SkyBlockPlayersOnlineModule;
import com.deyo.hypixel.type.generic.tab.AreaServerModule;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypeJerrysWorkshopLoader implements SkyBlockTypeLoader {
    @Override
    public ServerType getType() {
        return ServerType.SKYBLOCK_JERRYS_WORKSHOP;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        Logger.info("TypeJerrysWorkshopLoader initialized!");
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        GlassDisplay.create(new SkyBlockItem(ItemType.DIRT), HypixelConst.getInstanceContainer(), new Pos(14, 77, 91), (player, _) -> player.notImplemented());
        GlassDisplay.create(new SkyBlockItem(ItemType.DIRT), HypixelConst.getInstanceContainer(), new Pos(16, 77, 91), (player, _) -> player.notImplemented());
        GlassDisplay.create(new SkyBlockItem(ItemType.DIRT), HypixelConst.getInstanceContainer(), new Pos(13, 77, 97), (player, _) -> player.notImplemented());
        GlassDisplay.create(new SkyBlockItem(ItemType.DIRT), HypixelConst.getInstanceContainer(), new Pos(13, 77, 99), (player, _) -> player.notImplemented());
        GlassDisplay.create(new SkyBlockItem(ItemType.DIRT), HypixelConst.getInstanceContainer(), new Pos(13, 77, 101), (player, _) -> player.notImplemented());

        Present.spawnAll();
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
            (_) -> new Pos(-4.5, 76, 100.5, -180, 0), // Spawn position
            true // Announce death messages
        );
    }

    public TablistManager getTablistManager() {
        return new TablistManager() {
            @Override
            public List<TablistModule> getModules() {
                return new ArrayList<>(List.of(
                    new SkyBlockPlayersOnlineModule(1),
                    new SkyBlockPlayersOnlineModule(2),
                    new AreaServerModule("tablist.server_info.area.jerrys_workshop"),
                    new AccountInformationModule()
                ));
            }
        };
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
            "com.deyo.hypixel.type.jerrysworkshop.events",
            HypixelEventClass.class
        ).collect(Collectors.toList());
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return new ArrayList<>();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return new ArrayList<>(SkyBlockGenericLoader.loopThroughPackage(
            "com.deyo.hypixel.type.jerrysworkshop.npcs",
            HypixelNPC.class
        ).toList());
    }


    @Override
    public List<RedisMessageHandler<?, ?>> getProxyHandlers() {
        return List.of();
    }

    @Override
    public List<ServiceType> getRequiredServices() {
        return new ArrayList<>();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.SKYBLOCK_JERRYS_WORKSHOP;
    }
}
