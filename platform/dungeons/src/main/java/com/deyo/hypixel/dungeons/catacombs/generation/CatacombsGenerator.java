package com.deyo.hypixel.dungeons.catacombs.generation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.deyo.hypixel.dungeons.DungeonRoomType;
import com.deyo.hypixel.dungeons.DungeonsAPI;
import com.deyo.hypixel.dungeons.DungeonsData;
import com.deyo.hypixel.dungeons.GeneratorService;
import com.deyo.hypixel.dungeons.catacombs.CatacombsFloorDefinition;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CatacombsGenerator {
    public static GeneratorService generator(CatacombsFloorDefinition definition) {
        DungeonsData data = new DungeonsData(
                definition.floor().dungeonSize().width(),
                definition.floor().dungeonSize().height())
                .with(DungeonRoomType.FAIRY, new DungeonsData.RoomData(1, 1))
                .with(DungeonRoomType.PUZZLE, new DungeonsData.RoomData(definition.puzzleRooms(), definition.puzzleRooms()))
                .with(DungeonRoomType.MINI_BOSS, new DungeonsData.RoomData(definition.minibossRooms(), definition.minibossRooms()));

        if (definition.trapRooms() > 0) {
            data.with(DungeonRoomType.TRAP, new DungeonsData.RoomData(definition.trapRooms(), definition.trapRooms()));
        }

        return DungeonsAPI.getGeneratorService(data);
    }
}
