package com.deyo.hypixel.anticheat.engine;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeyoEngineTest {

    @AfterEach
    void cleanUp() {
        DeyoPlayer.players.clear();
    }

    @Test
    void stalePlayersAreThoseNoLongerOnline() {
        UUID online = UUID.randomUUID();
        UUID disconnected = UUID.randomUUID();
        new DeyoPlayer(online);
        new DeyoPlayer(disconnected);

        List<UUID> stale = DeyoEngine.stalePlayers(Set.of(online));

        assertEquals(List.of(disconnected), stale);
    }

    @Test
    void noPlayersAreStaleWhenEveryoneIsOnline() {
        UUID online = UUID.randomUUID();
        new DeyoPlayer(online);

        assertEquals(List.of(), DeyoEngine.stalePlayers(Set.of(online)));
    }
}
