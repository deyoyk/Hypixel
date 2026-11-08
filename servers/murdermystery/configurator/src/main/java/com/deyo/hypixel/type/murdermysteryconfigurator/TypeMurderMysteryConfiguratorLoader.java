package com.deyo.hypixel.type.murdermysteryconfigurator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import com.deyo.hypixel.commons.CustomWorlds;
import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.murdermystery.map.MurderMysteryMapsConfig;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.type.generic.HypixelGenericLoader;
import com.deyo.hypixel.type.generic.HypixelTypeLoader;
import com.deyo.hypixel.type.generic.command.HypixelCommand;
import com.deyo.hypixel.type.generic.entity.npc.HypixelNPC;
import com.deyo.hypixel.type.generic.event.HypixelEventClass;
import com.deyo.hypixel.type.generic.tab.EmptyTabModule;
import com.deyo.hypixel.type.generic.tab.TablistManager;
import com.deyo.hypixel.type.generic.tab.TablistModule;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TypeMurderMysteryConfiguratorLoader implements HypixelTypeLoader {
    private static Gson gson;

    @Getter
    private static MurderMysteryMapsConfig mapsConfig;

    @Override
    public ServerType getType() {
        return ServerType.MURDER_MYSTERY_CONFIGURATOR;
    }

    @Override
    public void onInitialize(MinecraftServer server) {
        gson = new GsonBuilder().create();
        loadMapsConfig();
    }

    private static void loadMapsConfig() {
        Path mapsPath = Path.of("./configuration/murdermystery/maps.json");
        if (!Files.exists(mapsPath)) {
            Logger.warn("maps.json not found at {}, creating empty config", mapsPath.toAbsolutePath());
            mapsConfig = new MurderMysteryMapsConfig();
            return;
        }
        try (InputStream in = Files.newInputStream(mapsPath)) {
            byte[] bytes = in.readAllBytes();
            String json = new String(bytes, StandardCharsets.UTF_8);
            mapsConfig = gson.fromJson(json, MurderMysteryMapsConfig.class);
            if (mapsConfig == null || mapsConfig.getMaps() == null) {
                mapsConfig = new MurderMysteryMapsConfig();
            }
        } catch (Exception e) {
            Logger.error("Failed to load maps.json", e);
            mapsConfig = new MurderMysteryMapsConfig();
        }
    }

    /**
     * Reloads the maps configuration from file
     */
    public static void reloadMapsConfig() {
        if (gson == null) {
            gson = new GsonBuilder().create();
        }
        loadMapsConfig();
        Logger.info("Reloaded maps.json configuration");
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        }
        return gson;
    }

    @Override
    public void afterInitialize(MinecraftServer server) {
        HypixelGenericLoader.loopThroughPackage("com.deyo.hypixel.type.murdermysteryconfigurator.commands", HypixelCommand.class).forEach(command -> {
            try {
                MinecraftServer.getCommandManager().register(command.getCommand());
            } catch (Exception e) {
                Logger.error(e, "Failed to register command " + command.getCommand().getName() + " in class " + command.getClass().getSimpleName());
            }
        });
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
                (type) -> new Pos(0, 100, 0, -90, 0), // Spawn position
                false // Announce death messages
        );
    }

    @Override
    public List<HypixelEventClass> getTraditionalEvents() {
        return HypixelGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.murdermysteryconfigurator.events",
                HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelEventClass> getCustomEvents() {
        return HypixelGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.murdermysteryconfigurator.events.custom",
                HypixelEventClass.class
        ).toList();
    }

    @Override
    public List<HypixelNPC> getNPCs() {
        return HypixelGenericLoader.loopThroughPackage(
                "com.deyo.hypixel.type.murdermysteryconfigurator.npcs",
                HypixelNPC.class
        ).toList();
    }


    @Override
    public List<RedisMessageHandler<?, ?>> getProxyHandlers() {
        return List.of();
    }

    @Override
    public @Nullable CustomWorlds getMainInstance() {
        return CustomWorlds.MURDER_MYSTERY_LOBBY;
    }
}
