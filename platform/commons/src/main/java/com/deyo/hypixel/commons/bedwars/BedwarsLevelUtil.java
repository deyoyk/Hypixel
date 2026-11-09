package com.deyo.hypixel.commons.bedwars;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BedwarsLevelUtil {

    private static final ThreadLocal<DecimalFormat> FORMAT = ThreadLocal.withInitial(() -> new DecimalFormat("#,###.#"));
    private static final int LEVELS_PER_CYCLE = 100;
    private static final long EXPERIENCE_PER_CYCLE = 487_000L;

    public static String suffix(double value) {
        if (value < 1000) {
            return FORMAT.get().format(value);
        } else {
            return FORMAT.get().format(value / 1000) + "k";
        }
    }

    public static int calculateMaxExperienceFromLevel(int level) {
        int multiplier = level % 100;
        if (multiplier > 5) {
            multiplier = 5;
        }

        return switch (multiplier) {
            case 0 -> 500;
            case 1 -> 1000;
            case 2 -> 2000;
            case 3 -> 3500;
            default -> 5000;
        };
    }

    public static int calculateLevel(long experience) {
        if (experience < 0) return 0;

        long completedCycles = experience / EXPERIENCE_PER_CYCLE;
        long remaining = experience % EXPERIENCE_PER_CYCLE;
        int level = Math.toIntExact(completedCycles * LEVELS_PER_CYCLE);

        for (int index = 0; index < LEVELS_PER_CYCLE; index++) {
            int required = calculateMaxExperienceFromLevel(index);
            if (remaining < required) break;
            remaining -= required;
            level++;
        }

        return level;
    }

    public static int calculateExperienceSinceLastLevel(long experience) {
        int level = calculateLevel(experience);
        return (int) (experience - totalExperienceForLevel(level));
    }

    public static int calculateMaxExperienceFromExperience(long experience) {
        return calculateMaxExperienceFromLevel(calculateLevel(experience));
    }

    static long totalExperienceForLevel(int level) {
        long completedCycles = level / LEVELS_PER_CYCLE;
        int remainingLevels = level % LEVELS_PER_CYCLE;
        long total = completedCycles * EXPERIENCE_PER_CYCLE;
        for (int index = 0; index < remainingLevels; index++) {
            total += calculateMaxExperienceFromLevel(index);
        }
        return total;
    }
}
