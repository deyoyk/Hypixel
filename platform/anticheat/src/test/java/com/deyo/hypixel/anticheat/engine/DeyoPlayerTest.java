package com.deyo.hypixel.anticheat.engine;

import com.deyo.hypixel.anticheat.loader.DeyoAnticheat;
import com.deyo.hypixel.anticheat.loader.DeyoValues;
import com.deyo.hypixel.anticheat.math.Vel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeyoPlayerTest {

    @AfterEach
    void cleanUp() {
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

    @Test
    void knockbackIsRecordedAndAgesWithMovementTicks() {
        DeyoPlayer player = new DeyoPlayer(UUID.randomUUID());
        Vel knockback = new Vel(0.4, 0.36, 0.1);

        player.handleKnockback(knockback);

        assertEquals(0, player.getTicksSinceKnockback());
        assertSame(knockback, player.getExpectedKnockback());

        player.moveTickOn();
        player.moveTickOn();

        assertEquals(2, player.getTicksSinceKnockback());
    }
}
