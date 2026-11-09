package com.deyo.hypixel.type.mainlobby;

import com.deyo.hypixel.type.generic.HypixelConst;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainLobbyScoreboardTest {

    @AfterEach
    void resetServerName() {
        HypixelConst.setServerName(null);
    }

    @Test
    void lobbyNumberComesFromTheServerNameDigits() {
        HypixelConst.setServerName("L1a");
        assertEquals(1, MainLobbyScoreboard.lobbyNumber());

        HypixelConst.setServerName("mini12A");
        assertEquals(12, MainLobbyScoreboard.lobbyNumber());

        HypixelConst.setServerName("isolated3");
        assertEquals(3, MainLobbyScoreboard.lobbyNumber());
    }

    @Test
    void lobbyNumberIsZeroWithoutADigitOrName() {
        HypixelConst.setServerName(null);
        assertEquals(0, MainLobbyScoreboard.lobbyNumber());

        HypixelConst.setServerName("Lobby");
        assertEquals(0, MainLobbyScoreboard.lobbyNumber());
    }
}
