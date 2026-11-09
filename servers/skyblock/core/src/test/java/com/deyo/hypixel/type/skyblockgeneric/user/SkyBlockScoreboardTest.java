package com.deyo.hypixel.type.skyblockgeneric.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SkyBlockScoreboardTest {

    @Test
    void noCurrentAuctionItemHasNoDisplay() {
        assertNull(SkyBlockScoreboard.getAuctionItemDisplay(null));
    }

    @Test
    void unknownAuctionItemFallsBackToReadableName() {
        assertEquals("NOT A REAL ITEM",
                SkyBlockScoreboard.getAuctionItemDisplay("NOT_A_REAL_ITEM").plain());
    }
}
