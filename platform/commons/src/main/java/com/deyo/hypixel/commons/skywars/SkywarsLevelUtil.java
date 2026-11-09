package com.deyo.hypixel.commons.skywars;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SkywarsLevelUtil {

    private static final ThreadLocal<DecimalFormat> FORMAT = ThreadLocal.withInitial(() -> new DecimalFormat("#,###.#"));
    private static final long EXPERIENCE_TO_LEVEL_15 = 3_000L;

    public static String suffix(double value) {
        if (value < 1000) {
            return FORMAT.get().format(value);
        } else {
            return FORMAT.get().format(value / 1000) + "k";
        }
    }

    /**
     * XP requirements per level:
     * Level 1-5: 100 XP each
     * Level 6-10: 200 XP each
     * Level 11-15: 300 XP each
     * Level 16+: 500 XP each
     */
    public static int calculateMaxExperienceFromLevel(int level) {
        if (level < 5) return 100;
        if (level < 10) return 200;
        if (level < 15) return 300;
        return 500;
    }

    public static int calculateLevel(long experience) {
        if (experience < 0) return 0;
        if (experience >= EXPERIENCE_TO_LEVEL_15) {
            return (int) (15 + (experience - EXPERIENCE_TO_LEVEL_15) / 500);
        }

        int level = 0;
        long totalExperience = 0;
        while (true) {
            int required = calculateMaxExperienceFromLevel(level);
            if (totalExperience + required > experience) break;
            totalExperience += required;
            level++;
        }
        return level;
    }

    public static int calculateExperienceSinceLastLevel(long experience) {
        int level = calculateLevel(experience);
        return (int) (experience - calculateTotalExperienceForLevel(level));
    }

    public static int calculateMaxExperienceFromExperience(long experience) {
        return calculateMaxExperienceFromLevel(calculateLevel(experience));
    }

    public static long calculateTotalExperienceForLevel(int targetLevel) {
        int firstTier = Math.min(targetLevel, 5);
        int secondTier = Math.clamp(targetLevel - 5, 0, 5);
        int thirdTier = Math.clamp(targetLevel - 10, 0, 5);
        int finalTier = Math.max(0, targetLevel - 15);
        return firstTier * 100L + secondTier * 200L + thirdTier * 300L + finalTier * 500L;
    }
}
