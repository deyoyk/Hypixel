package com.deyo.hypixel.type.bedwarsgame.item.impl;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.bedwars.BedWarsGameType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayAgainItemTest {

    @Test
    void requeueUsesTheCurrentModeAndMap() {
        assertEquals(
                new PlayAgainItem.RequeueTarget(ServerType.BEDWARS_GAME, "ONE_EIGHT", "Lighthouse"),
                PlayAgainItem.requeueTarget(BedWarsGameType.ONE_EIGHT, "Lighthouse")
        );
    }
}
