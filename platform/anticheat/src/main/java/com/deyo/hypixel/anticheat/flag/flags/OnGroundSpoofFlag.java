package com.deyo.hypixel.anticheat.flag.flags;

import com.deyo.hypixel.anticheat.event.ListenerMethod;
import com.deyo.hypixel.anticheat.event.events.PlayerPositionUpdateEvent;
import com.deyo.hypixel.anticheat.flag.Flag;

public class OnGroundSpoofFlag extends Flag {
    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        // Disabled: heuristic-based detection has too many false positives
        // Needs actual collision checking against world blocks to be accurate
    }
}
