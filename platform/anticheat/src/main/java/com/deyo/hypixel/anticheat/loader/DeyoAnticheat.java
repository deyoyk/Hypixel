package com.deyo.hypixel.anticheat.loader;

import lombok.Getter;
import com.deyo.hypixel.anticheat.engine.DeyoEngine;
import com.deyo.hypixel.anticheat.event.AntiCheatListener;
import com.deyo.hypixel.anticheat.event.DeyoEventHandler;
import com.deyo.hypixel.anticheat.flag.FlagType;

import java.util.UUID;

public class DeyoAnticheat {
    @Getter
    private static Loader loader = null;
    @Getter
    private static DeyoValues values = null;
    @Getter
    private static PunishmentHandler punishmentHandler = new PunishmentHandler() {
        @Override
        public void onFlag(UUID uuid, FlagType flagType) {}
    };

    public static void loader(Loader loader) {
        DeyoAnticheat.loader = loader;
    }

    public static void values(DeyoValues values) {
        DeyoAnticheat.values = values;
    }

    public static void punishmentHandler(PunishmentHandler punishmentHandler) {
        DeyoAnticheat.punishmentHandler = punishmentHandler;
    }

    public static void registerEvent(AntiCheatListener listener) {
        DeyoEventHandler.registerEventMethods(listener);
    }

    public static void start() {
        if (loader == null) {
            throw new IllegalStateException("Loader not set, use DeyoAnticheat#loader(Loader) to set the loader");
        }

        if (values == null) {
            throw new IllegalStateException("Values not set, use DeyoAnticheat#values(DeyoValues) to set the values");
        }

        loader.onInitialize();
        DeyoEngine.registerEvents();
        DeyoEngine.startSchedulers(loader);
    }
}
