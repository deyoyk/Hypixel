package com.deyo.hypixel.anticheat.engine;

import com.deyo.hypixel.anticheat.api.AnticheatAPI;
import com.deyo.hypixel.anticheat.event.DeyoEventHandler;
import com.deyo.hypixel.anticheat.flag.FlagType;
import com.deyo.hypixel.anticheat.flag.flags.AimFlag;
import com.deyo.hypixel.anticheat.flag.flags.AutoClickerFlag;
import com.deyo.hypixel.anticheat.flag.flags.BadPacketsFlag;
import com.deyo.hypixel.anticheat.flag.flags.KillAuraFlag;
import com.deyo.hypixel.anticheat.flag.flags.PredictionFlag;
import com.deyo.hypixel.anticheat.flag.flags.TimerFlag;
import com.deyo.hypixel.anticheat.flag.flags.VelocityFlag;
import com.deyo.hypixel.anticheat.loader.Loader;
import com.deyo.hypixel.anticheat.loader.DeyoAnticheat;
import com.deyo.hypixel.anticheat.loader.managers.DeyoSchedulerManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DeyoEngine {
    public static void startSchedulers(Loader loader) {
        DeyoSchedulerManager scheduler = loader.getSchedulerManager();
        scheduler.scheduleRepeatingTask(() -> {
            Set<UUID> onlinePlayers = new HashSet<>(loader.getOnlinePlayers());
            List<DeyoPlayer> players = new ArrayList<>(onlinePlayers.size());

            for (UUID uuid : stalePlayers(onlinePlayers)) {
                DeyoPlayer player = DeyoPlayer.players.remove(uuid);
                if (player == null) continue;
                player.getWorld().shutdown();
                // Clean up player data when they disconnect
                AnticheatAPI.clearPlayerData(uuid);
                AnticheatAPI.clearBypasses(uuid);
                TimerFlag.clearPlayer(uuid);
                VelocityFlag.clearPlayer(uuid);
                KillAuraFlag.clearPlayer(uuid);
                AimFlag.clearPlayer(uuid);
                AutoClickerFlag.clearPlayer(uuid);
                BadPacketsFlag.clearPlayer(uuid);
                PredictionFlag.clearPlayer(uuid);
            }

            for (UUID uuid : onlinePlayers) {
                DeyoPlayer player = DeyoPlayer.players.get(uuid);
                players.add(player != null ? player : new DeyoPlayer(uuid));
            }

            players.forEach((player -> {
                player.moveTickOn();
                player.sendPingRequest();
                if (player.ticksSinceLastPingResponse() > DeyoAnticheat.getValues().getTicksAllowedToMissPing()) {
                    player.flag(FlagType.TIMEOUT_PING_PACKETS, 1.0);
                }
            }));
        }, 1, 1);

        // Cleanup expired bypasses every 5 seconds (100 ticks)
        scheduler.scheduleRepeatingTask(() -> {
            AnticheatAPI.getBypassManager().cleanupExpired();
        }, 100, 100);
    }

    static List<UUID> stalePlayers(Set<UUID> onlinePlayers) {
        List<UUID> stale = new ArrayList<>();
        for (UUID uuid : DeyoPlayer.players.keySet()) {
            if (!onlinePlayers.contains(uuid)) {
                stale.add(uuid);
            }
        }
        return stale;
    }

    public static void registerEvents() {
        DeyoEventHandler.registerEventMethods(new MovementEvents());
        DeyoEventHandler.registerEventMethods(new PingEvents());
        DeyoEventHandler.registerEventMethods(new KnockbackEvents());

        Arrays.stream(FlagType.values()).forEach(flagType -> {
            DeyoEventHandler.registerEventMethods(flagType.getFlagSupplier().get());
        });
    }
}
