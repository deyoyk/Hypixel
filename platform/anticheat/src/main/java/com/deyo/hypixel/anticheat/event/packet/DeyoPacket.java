package com.deyo.hypixel.anticheat.event.packet;

import lombok.Getter;
import com.deyo.hypixel.anticheat.engine.DeyoPlayer;

import java.util.UUID;

@Getter
public abstract class DeyoPacket {
    private final DeyoPlayer player;

    public DeyoPacket(UUID uuid) {
        this.player = DeyoPlayer.players.get(uuid);
    }

    public DeyoPacket(DeyoPlayer player) {
        this.player = player;
    }
}
