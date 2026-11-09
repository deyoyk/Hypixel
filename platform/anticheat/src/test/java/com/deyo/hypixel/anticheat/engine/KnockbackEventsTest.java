package com.deyo.hypixel.anticheat.engine;

import com.deyo.hypixel.anticheat.event.events.AnticheatPacketEvent;
import com.deyo.hypixel.anticheat.event.events.PlayerKnockbackEvent;
import com.deyo.hypixel.anticheat.event.packet.KnockbackPacket;
import com.deyo.hypixel.anticheat.math.Vel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class KnockbackEventsTest {

    @AfterEach
    void cleanUp() {
        for (DeyoPlayer player : DeyoPlayer.players.values()) {
            player.getWorld().shutdown();
        }
        DeyoPlayer.players.clear();
    }

    @Test
    void knockbackPacketsBecomePlayerKnockbackEvents() {
        DeyoPlayer player = new DeyoPlayer(UUID.randomUUID());
        Vel velocity = new Vel(0.4, 0.36, 0.1);

        PlayerKnockbackEvent event = KnockbackEvents.toEvent(new AnticheatPacketEvent(
                new KnockbackPacket(player.getUuid(), velocity)));

        assertNotNull(event);
        assertSame(player, event.getPlayer());
        assertSame(velocity, event.getKnockbackVelocity());
    }
}
