package com.deyo.hypixel.type.generic.achievement;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AchievementRegistryTest {

    @Test
    void filtersSeasonalAchievementsForAllAndSingleCategories() {
        AchievementDefinition bedwarsSeasonal = AchievementDefinition.builder()
                .id("bw_seasonal")
                .name("Bed Wars Seasonal")
                .type(AchievementType.SEASONAL)
                .category(AchievementCategory.BEDWARS)
                .points(10)
                .build();
        AchievementDefinition skywarsSeasonal = AchievementDefinition.builder()
                .id("sw_seasonal")
                .name("SkyWars Seasonal")
                .type(AchievementType.SEASONAL)
                .category(AchievementCategory.SKYWARS)
                .points(15)
                .build();
        AchievementDefinition bedwarsChallenge = AchievementDefinition.builder()
                .id("bw_challenge")
                .name("Bed Wars Challenge")
                .type(AchievementType.CHALLENGE)
                .category(AchievementCategory.BEDWARS)
                .points(5)
                .build();
        List<AchievementDefinition> definitions = List.of(
                bedwarsSeasonal, skywarsSeasonal, bedwarsChallenge);

        List<AchievementDefinition> allSeasonal = AchievementRegistry.filterSeasonal(definitions, null);
        assertEquals(List.of(bedwarsSeasonal, skywarsSeasonal), allSeasonal);
        assertTrue(allSeasonal.stream().allMatch(a -> a.getType() == AchievementType.SEASONAL));

        assertEquals(List.of(bedwarsSeasonal),
                AchievementRegistry.filterSeasonal(definitions, AchievementCategory.BEDWARS));
    }
}
