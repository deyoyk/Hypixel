package com.deyo.hypixel.type.dwarvenmines;

import net.kyori.adventure.key.Key;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.DimensionType;
import com.deyo.hypixel.commons.CustomWorlds;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.commons.text.Text;
import com.deyo.hypixel.type.dwarvenmines.gui.GUIGemstoneGrinder;
import com.deyo.hypixel.type.generic.tab.AreaServerModule;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.generic.SkyBlockTypeLoader;
import com.deyo.hypixel.type.generic.entity.InteractionEntity;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.tab.TablistManager;
import com.deyo.hypixel.type.generic.tab.TablistModule;
import com.deyo.hypixel.type.skyblockgeneric.SkyBlockGenericLoader;
import com.deyo.hypixel.type.skyblockgeneric.entity.TextDisplayEntity;
import com.deyo.hypixel.type.skyblockgeneric.tabmodules.AccountInformationModule;
import com.deyo.hypixel.type.skyblockgeneric.tabmodules.SkyBlockPlayersOnlineModule;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypeDwarvenMinesLoader implements SkyBlockTypeLoader {
    @Override
    public ServerType getType() {
        return ServerType.SKYBLOCK_DWARVEN_MINES;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        Logger.info("TypeDwarvenMinesLoader initialized!");
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        Point gemstoneGrinderPos = new Pos(85.5, 199, -116.5);

        InteractionEntity gemstoneGrinder = new InteractionEntity(1.1f, 1.1f, (p, event) -> {
           new GUIGemstoneGrinder().open(p);
        });
        TextDisplayEntity gemstoneGrinderText = new TextDisplayEntity("<d>Gemstone Grinder", meta -> {});
        TextDisplayEntity gemstoneGrinderClick = new TextDisplayEntity("<e><l>CLICK", meta -> {});

        gemstoneGrinder.setInstance(HypixelConst.getInstanceContainer(), gemstoneGrinderPos);
        gemstoneGrinderText.setInstance(HypixelConst.getInstanceContainer(), gemstoneGrinderPos.add(0, 1.3, 0));
        gemstoneGrinderClick.setInstance(HypixelConst.getInstanceContainer(), gemstoneGrinderPos.add(0, 0.9, 0));
    }

	@Override
	public LoaderValues getLoaderValues() {
		return new LoaderValues(
				(_) -> new Pos(-48.5, 200, -121.5, -90, 0),
				true
		);
	}

    public TablistManager getTablistManager() {
        return new TablistManager() {
            @Override
            public List<TablistModule> getModules() {
                return new ArrayList<>(List.of(
                        new SkyBlockPlayersOnlineModule(1),
                        new SkyBlockPlayersOnlineModule(2),
                        new AreaServerModule("tablist.server_info.area.dwarven_mines"),
                        new AccountInformationModule()
                ));
            }
        };
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        return SkyBlockGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.dwarvenmines.events",
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
                "com.deyo.hypixel.type.dwarvenmines.npcs",
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
        return CustomWorlds.SKYBLOCK_DWARVEN_MINES;
    }

	@Override
	public @Nullable RegistryKey<DimensionType> getDimensionType() {
		return MinecraftServer.getDimensionTypeRegistry().register(
				Key.key("skyblock:dwarven_mines"),
				DimensionType.builder()
						.ambientLight(1f)
						.build());
	}

}
