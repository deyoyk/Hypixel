package com.deyo.hypixel.anticheat.flag.flags;

import com.deyo.hypixel.anticheat.event.ListenerMethod;
import com.deyo.hypixel.anticheat.event.events.PlayerPositionUpdateEvent;
import com.deyo.hypixel.anticheat.flag.Flag;

public class TimeoutPingPacketsFlag extends Flag {
    // This flag is triggered by the engine when ping packets timeout
    // We can add additional logic here for handling the timeout

    @ListenerMethod
    public void onPlayerPositionUpdate(PlayerPositionUpdateEvent event) {
        // Check if player has timed out on ping responses
        int ticksSinceLastPing = event.getPlayer().ticksSinceLastPingResponse();

        // This is already checked in DeyoEngine, but we can add more granular detection
        // For example, warn at lower thresholds

        if (ticksSinceLastPing > 30) {
            // Player is becoming unresponsive
            double certainty = Math.min(0.95, 0.5 + (ticksSinceLastPing - 30) * 0.01);
            event.getPlayer().flag(com.deyo.hypixel.anticheat.flag.FlagType.TIMEOUT_PING_PACKETS, certainty);
        }
    }
}
