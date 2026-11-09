package com.deyo.hypixel.dungeons;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CatacombsDungeonTest {

    @Test
    void reportsOnlyConnectedNeighbours() {
        CatacombsDungeon dungeon = new CatacombsDungeon();
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                dungeon.setRoom(x, y, CatacombsDungeon.DungeonRoom.ofBase());
            }
        }

        dungeon.connectRooms(0, 0, 1, 0);

        List<int[]> doors = dungeon.getDoors(0, 0);
        assertEquals(1, doors.size());
        assertArrayEquals(new int[]{1, 0}, doors.getFirst());
        assertTrue(dungeon.getDoors(0, 1).isEmpty());
    }

    @Test
    void connectionsAreBidirectional() {
        CatacombsDungeon dungeon = new CatacombsDungeon();
        dungeon.setRoom(0, 0, CatacombsDungeon.DungeonRoom.ofBase());
        dungeon.setRoom(0, 1, CatacombsDungeon.DungeonRoom.ofBase());

        dungeon.connectRooms(0, 0, 0, 1);

        assertTrue(dungeon.isConnected(0, 0, 0, 1));
        assertTrue(dungeon.isConnected(0, 1, 0, 0));
        assertEquals(1, dungeon.getDoors(0, 1).size());
    }

    @Test
    void duplicateConnectionsAreStoredOnce() {
        CatacombsDungeon dungeon = new CatacombsDungeon();
        dungeon.setRoom(0, 0, CatacombsDungeon.DungeonRoom.ofBase());
        dungeon.setRoom(1, 0, CatacombsDungeon.DungeonRoom.ofBase());

        dungeon.connectRooms(0, 0, 1, 0);
        dungeon.connectRooms(0, 0, 1, 0);
        dungeon.connectRooms(1, 0, 0, 0);

        assertEquals(1, dungeon.getDoorConnections().size());
        assertEquals(1, dungeon.getDoors(0, 0).size());
        assertEquals(1, dungeon.getDoors(1, 0).size());
    }
}
