package com.deyo.hypixel.dungeons.catacombs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.deyo.hypixel.dungeons.GeneratorService;
import com.deyo.hypixel.dungeons.CatacombsDungeon;
import com.deyo.hypixel.dungeons.catacombs.classes.DungeonClassDefinition;
import com.deyo.hypixel.dungeons.catacombs.classes.DungeonClassRegistry;
import com.deyo.hypixel.dungeons.catacombs.classes.DungeonClassType;
import com.deyo.hypixel.dungeons.catacombs.generation.CatacombsGenerator;
import com.deyo.hypixel.dungeons.catacombs.instance.CatacombsInstanceService;
import com.deyo.hypixel.dungeons.catacombs.item.DungeonOrbProfile;
import com.deyo.hypixel.dungeons.catacombs.kit.DungeonClassKit;
import com.deyo.hypixel.dungeons.catacombs.kit.DungeonKitRegistry;
import com.deyo.hypixel.dungeons.catacombs.map.DungeonMapRenderResult;
import com.deyo.hypixel.dungeons.catacombs.map.DungeonMapRenderer;
import com.deyo.hypixel.dungeons.catacombs.mob.DungeonMobDefinition;
import com.deyo.hypixel.dungeons.catacombs.mob.DungeonMobRegistry;
import com.deyo.hypixel.dungeons.catacombs.mob.DungeonMobRole;
import com.deyo.hypixel.dungeons.catacombs.party.PartyFinderService;
import com.deyo.hypixel.dungeons.catacombs.puzzle.CatacombsPuzzle;
import com.deyo.hypixel.dungeons.catacombs.puzzle.CatacombsPuzzleFactory;
import com.deyo.hypixel.dungeons.catacombs.puzzle.PuzzleController;
import com.deyo.hypixel.dungeons.catacombs.run.CatacombsRunConfig;
import com.deyo.hypixel.dungeons.catacombs.run.CatacombsRunState;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CatacombsAPI {
    private static final DungeonClassRegistry CLASS_REGISTRY = DungeonClassRegistry.defaults();
    private static final CatacombsRegistry CATACOMBS_REGISTRY = CatacombsRegistry.defaults();
    private static final DungeonMobRegistry MOB_REGISTRY = DungeonMobRegistry.defaults();
    private static final DungeonKitRegistry KIT_REGISTRY = DungeonKitRegistry.defaults();
    private static final DungeonMapRenderer MAP_RENDERER = new DungeonMapRenderer();

    public static CatacombsFloorDefinition floor(CatacombsFloor floor, CatacombsMode mode) {
        return CATACOMBS_REGISTRY.floor(floor, mode);
    }

    public static DungeonClassDefinition dungeonClass(DungeonClassType type) {
        return CLASS_REGISTRY.definition(type);
    }

    public static Map<DungeonClassType, DungeonClassDefinition> classes() {
        return CLASS_REGISTRY.definitions();
    }

    public static DungeonOrbProfile dungeonOrb(DungeonClassType type) {
        return DungeonOrbProfile.forClass(dungeonClass(type));
    }

    public static List<DungeonClassKit> kits(DungeonClassType type) {
        return KIT_REGISTRY.kits(type);
    }

    public static DungeonClassKit kit(String id) {
        return KIT_REGISTRY.kit(id);
    }

    public static PartyFinderService partyFinder() {
        return new PartyFinderService();
    }

    public static CatacombsInstanceService instances() {
        return new CatacombsInstanceService();
    }

    public static List<DungeonMobDefinition> mobs(CatacombsFloor floor, DungeonMobRole role) {
        return MOB_REGISTRY.spawnable(floor, role);
    }

    public static DungeonMobDefinition mob(String id) {
        return MOB_REGISTRY.mob(id);
    }

    public static PuzzleController puzzle(CatacombsPuzzle puzzle) {
        return CatacombsPuzzleFactory.create(puzzle);
    }

    public static GeneratorService generator(CatacombsFloorDefinition definition) {
        return CatacombsGenerator.generator(definition);
    }

    public static DungeonMapRenderResult renderMap(CatacombsDungeon dungeon, Path outputPath) throws IOException {
        return MAP_RENDERER.renderPng(dungeon, outputPath);
    }

    public static CatacombsRunState startRun(CatacombsRunConfig config) {
        return CatacombsRunState.start(config);
    }
}
