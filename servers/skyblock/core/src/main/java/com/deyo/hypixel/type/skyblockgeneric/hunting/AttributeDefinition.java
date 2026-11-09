package com.deyo.hypixel.type.skyblockgeneric.hunting;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.MobType;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;

import java.util.List;

public record AttributeDefinition(
        AttributeId id,
        String shard,
        String name,
        AttributeRarity rarity,
        AttributeCategory category,
        List<AttributeFamily> families,
        AttributeSkill skill,
        List<AttributeEffect> effects,
        List<AttributeSource> sources,
        String effectDescription,
        List<String> huntInfo
) {
    public AttributeDefinition {
        families = families == null || families.isEmpty() ? List.of(AttributeFamily.NONE) : List.copyOf(families);
        effects = List.copyOf(effects);
        sources = List.copyOf(sources);
        huntInfo = List.copyOf(huntInfo);
    }

    public boolean hasFamily(AttributeFamily family) {
        return families.contains(family);
    }

    public String shardName() {
        return shard + " Shard";
    }

    /**
     * Human-readable presentation text; mechanics are represented by {@link #effects()}.
     */
    public String effect() {
        return effectDescription;
    }

    public int numericId() {
        return id.number();
    }

    public enum AttributeCategory {
        FOREST("<a>"), WATER("<b>"), COMBAT("<c>");
        private final String color;

        AttributeCategory(String color) {
            this.color = color;
        }

        public String color() {
            return color;
        }
    }

    public enum AttributeFamily {
        NONE, ELEMENTAL, PHANTOM, TROPICAL_FISH, LAPIS, SHULKER, REPTILE, AMPHIBIAN,
        LIZARD, SERPENT, BOX, SHARPENING, RESISTANCE, ATOMIZED, ESSENCE, RULER, WISDOM,
        ECHO, DEMON, EEL, OTHER
    }

    public enum AttributeSkill {GLOBAL, COMBAT, FARMING, FISHING, MINING, FORAGING, ENCHANTING, HUNTING, TAMING, ALCHEMY}

    public enum EffectType {STATISTIC_PER_LEVEL, PERCENTAGE_STATISTIC_PER_LEVEL, ABILITY_VALUE}

    public enum Condition {ALWAYS, DAY, NIGHT, FORAGING_ISLAND, FISHING_ISLAND, COMBAT_ISLAND, END}

    public record AttributeEffect(EffectType type, ItemStatistic statistic, double minimum, double maximum,
                                  Condition condition, MobType mobType, String key) {
        public double atLevel(int level) {
            if (level <= 1) return minimum;
            return minimum + (maximum - minimum) * (Math.min(level, 10) - 1) / 9D;
        }
    }

    public enum SourceType {MOB, LASSO, TRAP, SALT, FUSION, FISHING, FISHING_NET, BLACK_HOLE, TREE_GIFT, OTHER}

    public record AttributeSource(SourceType type, List<String> mobIds, List<RegionType> locations,
                                  ServerType island) {
        public AttributeSource {
            mobIds = List.copyOf(mobIds);
            locations = List.copyOf(locations);
        }
    }
}
