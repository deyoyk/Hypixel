package com.deyo.hypixel.type.lobby.gui;

import com.deyo.hypixel.type.generic.achievement.AchievementCategory;
import com.deyo.hypixel.type.generic.achievement.AchievementData;
import com.deyo.hypixel.type.generic.achievement.AchievementDefinition;
import com.deyo.hypixel.type.generic.achievement.AchievementType;
import net.minestom.server.MinecraftServer;
import net.minestom.server.inventory.InventoryType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GUITrackedAchievementsTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void collectsTrackedDefinitionsInCategoryOrder() {
        AchievementDefinition bedwars = definition("bw_tracked", AchievementCategory.BEDWARS);
        AchievementDefinition skywars = definition("sw_tracked", AchievementCategory.SKYWARS);
        AchievementData data = new AchievementData();
        data.setTrackedAchievement(AchievementCategory.BEDWARS, bedwars.getId());
        data.setTrackedAchievement(AchievementCategory.SKYWARS, skywars.getId());
        data.setTrackedAchievement(AchievementCategory.GENERAL, "missing");

        assertEquals(List.of(bedwars, skywars), GUITrackedAchievements.trackedAchievements(
                data, Map.of(bedwars.getId(), bedwars, skywars.getId(), skywars)::get));
    }

    @Test
    void noTrackedDefinitionsProducesAnEmptyList() {
        assertTrue(GUITrackedAchievements.trackedAchievements(new AchievementData()).isEmpty());
    }

    @Test
    void menuUsesFourRows() {
        assertEquals(InventoryType.CHEST_4_ROW, new GUITrackedAchievements().getSize());
    }

    private static AchievementDefinition definition(String id, AchievementCategory category) {
        return AchievementDefinition.builder()
                .id(id)
                .name(id)
                .type(AchievementType.TIERED)
                .category(category)
                .points(10)
                .build();
    }
}
