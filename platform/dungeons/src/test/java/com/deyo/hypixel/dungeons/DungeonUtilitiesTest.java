package com.deyo.hypixel.dungeons;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DungeonUtilitiesTest {

    @Test
    void iteratesAllRoomsForNonSquareDungeons() {
        DungeonsData data = new DungeonsData(2, 3);

        Set<Map.Entry<Integer, Integer>> entries = DungeonUtilities.loopOverDungeonRooms(data)
                .collect(Collectors.toSet());

        assertEquals(Set.of(
                Map.entry(0, 0), Map.entry(1, 0),
                Map.entry(0, 1), Map.entry(1, 1),
                Map.entry(0, 2), Map.entry(1, 2)
        ), entries);
    }

    @Test
    void aStarFindsShortestPath() {
        List<int[]> path = DungeonUtilities.aStar(List.of(), 0, 0, 2, 2, 3, 3);

        assertEquals(5, path.size());
        assertArrayEquals(new int[]{0, 0}, path.getFirst());
        assertArrayEquals(new int[]{2, 2}, path.getLast());
    }

    @Test
    void aStarAvoidsVisitedCells() {
        List<int[]> path = DungeonUtilities.aStar(
                List.of(new int[]{1, 0}, new int[]{1, 1}), 0, 0, 2, 0, 3, 3);

        assertFalse(path.isEmpty());
        for (int[] cell : path) {
            assertFalse(cell[0] == 1 && (cell[1] == 0 || cell[1] == 1));
        }
        assertArrayEquals(new int[]{2, 0}, path.getLast());
    }
}
