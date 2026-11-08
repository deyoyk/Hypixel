package com.deyo.hypixel.anticheat.loader;

import com.deyo.hypixel.anticheat.event.packet.DeyoPacket;
import com.deyo.hypixel.anticheat.loader.managers.DeyoSchedulerManager;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class Loader {
    private final Map<String, LoaderPacketHandler> packetHandlerMap = new HashMap<>();

    public abstract DeyoSchedulerManager getSchedulerManager();
    public abstract List<UUID> getOnlinePlayers();
    public abstract void onInitialize();
    public abstract void sendPacket(UUID uuid, DeyoPacket packet);
    public abstract void sendMessage(UUID uuid, String message);

    public void registerPacketHandler(Class<? extends DeyoPacket> clazz,
                                      LoaderPacketHandler packetHandler) {
        packetHandlerMap.put(clazz.getSimpleName(), packetHandler);
    }

    public @Nullable LoaderPacketHandler getPacketHandler(Class clazz) {
        for (Map.Entry<String, LoaderPacketHandler> entry : packetHandlerMap.entrySet()) {
            if (entry.getValue().getHandledPacketClass().equals(clazz)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public @Nullable LoaderPacketHandler getPacketHandler(DeyoPacket packet) {
        return packetHandlerMap.get(packet.getClass().getSimpleName());
    }
}
