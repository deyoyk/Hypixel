package com.deyo.hypixel.type.skyblockgeneric.block.placement.states;

import com.deyo.hypixel.type.skyblockgeneric.block.placement.states.state.WallHeight;
import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WallStateTest {

    @Test
    void updatesEveryWallSide() {
        WallState state = new WallState(Block.COBBLESTONE_WALL);

        state.setUp(true);
        state.setNorth(WallHeight.LOW);
        state.setSouth(WallHeight.TALL);
        state.setEast(WallHeight.LOW);
        state.setWest(WallHeight.NONE);

        assertEquals(true, state.getUp());
        assertEquals(WallHeight.LOW, state.getNorth());
        assertEquals(WallHeight.TALL, state.getSouth());
        assertEquals(WallHeight.LOW, state.getEast());
        assertEquals(WallHeight.NONE, state.getWest());
        assertEquals("true", state.block().properties().get("up"));
    }
}
