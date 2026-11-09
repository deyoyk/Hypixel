package com.deyo.hypixel.anticheat.flag;

import lombok.Getter;
import lombok.Setter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;
import com.deyo.hypixel.anticheat.event.AntiCheatListener;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public abstract class Flag extends AntiCheatListener {
    private final long timestamp;

    private double certainty = 0;

    public Flag() {
        this.timestamp = System.currentTimeMillis();
    }

    protected static @Nullable Player onlinePlayer(DeyoPlayer player) {
        return MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(player.getUuid());
    }
}
