package com.deyo.hypixel.dungeons;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GeneratorServiceTest {

    @Test
    void generatedDungeonContainsEntranceExitAndFairy() throws Exception {
        GeneratorService service = new GeneratorService(new DungeonsData(5, 5));

        CatacombsDungeon dungeon = service.generate().get(10, TimeUnit.SECONDS);

        assertNotNull(dungeon);
        assertEquals(1, count(dungeon, DungeonRoomType.ENTRANCE));
        assertEquals(1, count(dungeon, DungeonRoomType.EXIT));
        assertEquals(1, count(dungeon, DungeonRoomType.FAIRY));
    }

    @Test
    void assignsRequestedRoomsToSingleDoorRooms() {
        GeneratorService service = new GeneratorService(new DungeonsData(3, 3));
        CatacombsDungeon dungeon = baseDungeon(3, 3);
        dungeon.connectRooms(0, 0, 1, 0);
        dungeon.connectRooms(1, 0, 2, 0);

        service.assignRooms(Map.of(DungeonRoomType.PUZZLE, 2), dungeon);

        long puzzles = dungeon.getRooms().values().stream()
                .filter(room -> room.getRoomType() == DungeonRoomType.PUZZLE)
                .count();
        assertEquals(2, puzzles);
    }

    @Test
    void assignmentStopsWhenNoRoomCanFit() {
        GeneratorService service = new GeneratorService(new DungeonsData(1, 1));
        CatacombsDungeon dungeon = baseDungeon(1, 1);
        dungeon.getRoom(0, 0).setCritical(true);

        service.assignRooms(Map.of(DungeonRoomType.PUZZLE, 5), dungeon);

        assertEquals(DungeonRoomType.BASE, dungeon.getRoom(0, 0).getRoomType());
    }

    @Test
    void roomAmountsIncludeTheConfiguredMaximum() {
        DungeonsData.RoomData roomData = new DungeonsData.RoomData(2, 4);
        Random random = new Random(12345);
        Set<Integer> amounts = new HashSet<>();

        for (int i = 0; i < 200; i++) {
            amounts.add(GeneratorService.randomRoomAmount(roomData, random));
        }

        assertEquals(Set.of(2, 3, 4), amounts);
    }

    @Test
    void fixedRoomAmountsStayFixed() {
        assertEquals(3, GeneratorService.randomRoomAmount(new DungeonsData.RoomData(3, 3), new Random(1)));
    }

    @Test
    void assignRoomsConsumesEachEligibleRoomOnlyOnce() {
        GeneratorService service = new GeneratorService(new DungeonsData(3, 1));
        CatacombsDungeon dungeon = baseDungeon(3, 1);
        dungeon.connectRooms(0, 0, 1, 0);
        dungeon.connectRooms(1, 0, 2, 0);

        service.assignRooms(Map.of(
                DungeonRoomType.PUZZLE, 1,
                DungeonRoomType.MINI_BOSS, 1
        ), dungeon);

        assertEquals(1, count(dungeon, DungeonRoomType.PUZZLE));
        assertEquals(1, count(dungeon, DungeonRoomType.MINI_BOSS));
        assertEquals(DungeonRoomType.BASE, dungeon.getRoom(1, 0).getRoomType());
    }

    @Test
    void assignRoomsIsLimitedByTheEligibleRoomCount() {
        GeneratorService service = new GeneratorService(new DungeonsData(3, 1));
        CatacombsDungeon dungeon = baseDungeon(3, 1);
        dungeon.connectRooms(0, 0, 1, 0);
        dungeon.connectRooms(1, 0, 2, 0);

        service.assignRooms(Map.of(DungeonRoomType.PUZZLE, 5), dungeon);

        assertEquals(2, count(dungeon, DungeonRoomType.PUZZLE));
        assertEquals(DungeonRoomType.BASE, dungeon.getRoom(1, 0).getRoomType());
    }

    private static CatacombsDungeon baseDungeon(int width, int height) {
        CatacombsDungeon dungeon = new CatacombsDungeon();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                dungeon.setRoom(x, y, CatacombsDungeon.DungeonRoom.ofBase());
            }
        }
        return dungeon;
    }

    private static long count(CatacombsDungeon dungeon, DungeonRoomType type) {
        return dungeon.getRooms().values().stream()
                .filter(room -> room.getRoomType() == type)
                .count();
    }
}
