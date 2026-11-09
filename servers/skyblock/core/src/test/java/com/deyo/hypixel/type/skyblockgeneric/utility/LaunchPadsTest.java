package com.deyo.hypixel.type.skyblockgeneric.utility;

import net.minestom.server.coordinate.Pos;
import org.junit.jupiter.api.Test;
import com.deyo.hypixel.commons.ServerType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LaunchPadsTest {

    @Test
    void villageToGoldMineUsesTheGoldMineSpawn() {
        LaunchPads pad = LaunchPads.VILLAGE_TO_GOLD_MINE;
        Pos destination = pad.getDestination();

        assertEquals(ServerType.SKYBLOCK_GOLD_MINE, pad.getTargetServerType());
        assertEquals(-4.5, destination.x());
        assertEquals(74, destination.y());
        assertEquals(-278.5, destination.z());
        assertTrue(destination.sameView(new Pos(-4.5, 74, -278.5, -180, 0)));
    }
}
