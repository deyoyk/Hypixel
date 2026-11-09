package com.deyo.hypixel.commons.bedwars;

import com.deyo.hypixel.commons.skywars.SkywarsLevelUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LevelUtilTest {

    @Test
    void bedwarsLevelMatchesTheReferenceLoop() {
        for (long experience = 0; experience <= 60_000; experience += 37) {
            assertEquals(referenceBedwarsLevel(experience), BedwarsLevelUtil.calculateLevel(experience), "xp=" + experience);
        }
        for (long experience = 480_000; experience <= 520_000; experience += 53) {
            assertEquals(referenceBedwarsLevel(experience), BedwarsLevelUtil.calculateLevel(experience), "xp=" + experience);
        }
    }

    @Test
    void bedwarsProgressMatchesTheReferenceLoop() {
        for (long experience = 0; experience <= 20_000; experience += 97) {
            int level = referenceBedwarsLevel(experience);
            long total = 0;
            for (int index = 0; index < level; index++) {
                total += BedwarsLevelUtil.calculateMaxExperienceFromLevel(index);
            }
            assertEquals((int) (experience - total), BedwarsLevelUtil.calculateExperienceSinceLastLevel(experience));
        }
    }

    @Test
    void skywarsLevelMatchesTheReferenceLoop() {
        for (long experience = 0; experience <= 20_000; experience += 41) {
            assertEquals(referenceSkywarsLevel(experience), SkywarsLevelUtil.calculateLevel(experience), "xp=" + experience);
        }
    }

    @Test
    void skywarsTotalExperienceMatchesTheReferenceLoop() {
        for (int level = 0; level <= 100; level++) {
            long total = 0;
            for (int index = 0; index < level; index++) {
                total += SkywarsLevelUtil.calculateMaxExperienceFromLevel(index);
            }
            assertEquals(total, SkywarsLevelUtil.calculateTotalExperienceForLevel(level));
        }
    }

    private static int referenceBedwarsLevel(long experience) {
        int level = 0;
        long total = 0;
        while (total <= experience) {
            total += BedwarsLevelUtil.calculateMaxExperienceFromLevel(level);
            if (total <= experience) level++;
        }
        return level;
    }

    private static int referenceSkywarsLevel(long experience) {
        int level = 0;
        long total = 0;
        while (total <= experience) {
            total += SkywarsLevelUtil.calculateMaxExperienceFromLevel(level);
            if (total <= experience) level++;
        }
        return level;
    }
}
