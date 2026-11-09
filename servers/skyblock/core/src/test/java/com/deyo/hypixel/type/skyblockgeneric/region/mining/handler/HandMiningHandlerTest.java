package com.deyo.hypixel.type.skyblockgeneric.region.mining.handler;

import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HandMiningHandlerTest {

    @Test
    void handBreakableBlocksAcceptAnyToolOrAnEmptyHand() {
        HandMiningHandler handler = new HandMiningHandler(10);

        assertTrue(handler.canToolBreak(null));
        assertEquals(0, handler.getMiningPowerRequirement());
        assertEquals(ItemStatistic.MINING_SPEED, handler.getSpeedStatistic());
        assertFalse(handler.breaksInstantly());
        assertEquals("Hand", handler.getHandlerName());
    }

    @Test
    void factoryCreatesAHandHandler() {
        assertInstanceOf(HandMiningHandler.class, MiningHandlers.hand(10));
    }
}
