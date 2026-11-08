package com.deyo.hypixel.anticheat.engine;

import com.deyo.hypixel.anticheat.api.AnticheatAPI;
import com.deyo.hypixel.anticheat.event.DeyoEventHandler;
import com.deyo.hypixel.anticheat.flag.FlagType;
import com.deyo.hypixel.anticheat.loader.Loader;
import com.deyo.hypixel.anticheat.loader.DeyoAnticheat;
import com.deyo.hypixel.anticheat.loader.managers.DeyoSchedulerManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DeyoEngine {
    public static void startSchedulers(Loader loader) {
        DeyoSchedulerManager scheduler = loader.getSchedulerManager();
        scheduler.scheduleRepeatingTask(() -> {
            List<UUID> onlinePlayers = loader.getOnlinePlayers();
            List<DeyoPlayer> players = new ArrayList<>();

            synchronized (DeyoPlayer.players) {
                DeyoPlayer.players.forEach((uuid, player) -> {
                    if (!onlinePlayers.contains(uuid)) {
                        player.getWorld().shutdown();
                        DeyoPlayer.players.remove(uuid);
                        // Clean up player data when they disconnect
                        AnticheatAPI.clearPlayerData(uuid);
                        AnticheatAPI.clearBypasses(uuid);
                    }
                });
            }

            onlinePlayers.forEach(uuid -> {
                if (!DeyoPlayer.players.containsKey(uuid)) {
                    players.add(new DeyoPlayer(uuid));
                } else {
                    players.add(DeyoPlayer.players.get(uuid));
                }
            });

            players.forEach((player -> {
                player.moveTickOn();
                player.sendPingRequest();
                if (player.ticksSinceLastPingResponse() > DeyoAnticheat.getValues().getTicksAllowedToMissPing()) {
                    player.flag(FlagType.TIMEOUT_PING_PACKETS, 100);
                }
            }));
        }, 1, 1);

        // Cleanup expired bypasses every 5 seconds (100 ticks)
        scheduler.scheduleRepeatingTask(() -> {
            AnticheatAPI.getBypassManager().cleanupExpired();
        }, 100, 100);
    }

    public static void registerEvents() {
        DeyoEventHandler.registerEventMethods(new MovementEvents());
        DeyoEventHandler.registerEventMethods(new PingEvents());

        Arrays.stream(FlagType.values()).forEach(flagType -> {
            DeyoEventHandler.registerEventMethods(flagType.getFlagSupplier().get());
        });
    }
}
