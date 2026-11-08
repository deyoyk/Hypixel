package com.deyo.hypixel.type.deepcaverns;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.commons.CustomWorlds;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.type.generic.tab.AreaServerModule;
import com.deyo.hypixel.type.generic.SkyBlockTypeLoader;

import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;

import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.tab.TablistManager;
import com.deyo.hypixel.type.generic.tab.TablistModule;
import com.deyo.hypixel.type.skyblockgeneric.SkyBlockGenericLoader;
import com.deyo.hypixel.type.skyblockgeneric.tabmodules.AccountInformationModule;
import com.deyo.hypixel.type.skyblockgeneric.tabmodules.SkyBlockPlayersOnlineModule;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TypeDeepCavernsLoader implements SkyBlockTypeLoader {
	@Override
	public ServerType getType() {
		return ServerType.SKYBLOCK_DEEP_CAVERNS;
	}

	@Override
	public void onInitialize(MinecraftServer server) {
		Logger.info("TypeDeepCavernsLoader initialized!");
	}

	@Override
	public void afterInitialize(MinecraftServer server) {

	}

	@Override
	public LoaderValues getLoaderValues() {
		return new LoaderValues(
				(type) -> switch (type) {
					default -> new Pos(4, 157, 85, -180, 0);
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
						new AreaServerModule("tablist.server_info.area.deep_caverns"),
						new AccountInformationModule()
				));
			}
		};
	}

	@Override
	public List<HypixelEventClass> getTraditionalEvents() {
		return SkyBlockGenericLoader.loopThroughPackage(
				"com.deyo.hypixel.type.deepcaverns.events",
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
                "com.deyo.hypixel.type.deepcaverns.npcs",
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
		return CustomWorlds.SKYBLOCK_DEEP_CAVERNS;
	}
}
