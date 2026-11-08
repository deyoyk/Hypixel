package com.deyo.hypixel.type.goldmine;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.timer.TaskSchedule;
import com.deyo.hypixel.commons.CustomWorlds;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.SkyBlockTypeLoader;

import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;

import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.tab.TablistManager;
import com.deyo.hypixel.type.generic.tab.TablistModule;
import com.deyo.hypixel.type.goldmine.entity.EntityLostPickaxe;
import com.deyo.hypixel.type.generic.tab.AreaServerModule;
import com.deyo.hypixel.type.skyblockgeneric.SkyBlockGenericLoader;
import com.deyo.hypixel.type.skyblockgeneric.tabmodules.AccountInformationModule;
import com.deyo.hypixel.type.skyblockgeneric.tabmodules.SkyBlockPlayersOnlineModule;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypeGoldMineLoader implements SkyBlockTypeLoader {
    @Override
    public ServerType getType() {
        return ServerType.SKYBLOCK_GOLD_MINE;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        Logger.info("TypeGoldMineLoader initialized!");
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        EntityLostPickaxe lostPickaxe = new EntityLostPickaxe();
        lostPickaxe.setInstance(HypixelConst.getInstanceContainer(), new Pos(-18.9, 24.5, -305.1));

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            if (lostPickaxe.isRemoved()) return;
            lostPickaxe.getInstance().sendGroupedPacket(
                    new ParticlePacket(
                            Particle.CRIT,
                            lostPickaxe.getPosition().add(0, 0, -0.3),
                            new Pos(0.3, 0.2, 0.3),
                            0.06f, 3
                    )
            );
        }).repeat(TaskSchedule.tick(20)).schedule();
    }

    @Override
    public LoaderValues getLoaderValues() {
        return new LoaderValues(
                (type) -> switch (type) {
                    case SKYBLOCK_DEEP_CAVERNS -> new Pos(-8, 68, -393, 41, 0);
                    default -> new Pos(-4.5, 74, -278.5, -180, 0);
                }, // Spawn position
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
                        new AreaServerModule("tablist.server_info.area.gold_mine"),
                        new AccountInformationModule()
                ));
            }
        };
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.goldmine.events",
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
                "com.deyo.hypixel.type.goldmine.npcs",
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
        return CustomWorlds.SKYBLOCK_GOLD_MINE;
    }
}
