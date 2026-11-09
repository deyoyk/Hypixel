package com.deyo.hypixel.anticheat.engine;

import com.deyo.hypixel.anticheat.loader.DeyoAnticheat;
import com.deyo.hypixel.anticheat.loader.DeyoValues;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeyoPlayerTest {

    @AfterEach
    void cleanUp() {
        for (DeyoPlayer player : DeyoPlayer.players.values()) {
            player.getWorld().shutdown();
        }
        DeyoPlayer.players.clear();
    }

    @Test
    void pingAgeIsReportedInTicks() {
        assertEquals(0, DeyoPlayer.ticksSince(0, 49, 50));
        assertEquals(1, DeyoPlayer.ticksSince(0, 50, 50));
        assertEquals(40, DeyoPlayer.ticksSince(0, 2000, 50));
        assertEquals(0, DeyoPlayer.ticksSince(2000, 1000, 50));
        assertEquals(2, DeyoPlayer.ticksSince(0, 100, 0));
    }

    @Test
    void newPlayersDoNotStartWithAnExpiredPing() {
        DeyoValues previous = DeyoAnticheat.getValues();
        DeyoAnticheat.values(new DeyoValues());
        try {
            DeyoPlayer player = new DeyoPlayer(UUID.randomUUID());

            assertTrue(player.ticksSinceLastPingResponse() < 5);
        } finally {
            DeyoAnticheat.values(previous);
        }
    }
}
