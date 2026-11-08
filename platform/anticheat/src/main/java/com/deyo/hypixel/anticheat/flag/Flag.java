package com.deyo.hypixel.anticheat.flag;

import lombok.Getter;
import lombok.Setter;
import com.deyo.hypixel.anticheat.event.AntiCheatListener;

@Getter
@Setter
public abstract class Flag extends AntiCheatListener {
    private final long timestamp;

    private double certainty = 0;

    public Flag() {
        this.timestamp = System.currentTimeMillis();
    }
}
