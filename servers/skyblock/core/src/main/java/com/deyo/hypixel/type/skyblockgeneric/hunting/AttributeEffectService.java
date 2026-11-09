package com.deyo.hypixel.type.skyblockgeneric.hunting;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.generic.HypixelConst;
import com.deyo.hypixel.type.skyblockgeneric.calendar.SkyBlockCalendar;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointHunting;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.MobType;
import com.deyo.hypixel.type.skyblockgeneric.entity.mob.SkyBlockMob;
import com.deyo.hypixel.type.skyblockgeneric.user.SkyBlockPlayer;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import static com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeDefinition.*;

public final class AttributeEffectService {
    private static final AttributeId ECHO = AttributeId.parse("L6");
    private static final AttributeId ELEMENTAL_BONUS = AttributeId.parse("L44");

    private static final AttributeId RANGED_DAMAGE = AttributeId.parse("R30");
    private static final AttributeId MELEE_DAMAGE = AttributeId.parse("E27");
    private static final AttributeId SKELETAL_DAMAGE = AttributeId.parse("C12");
    private static final AttributeId UNDEAD_DAMAGE = AttributeId.parse("U24");
    private static final AttributeId WOODLAND_DAMAGE = AttributeId.parse("U29");
    private static final AttributeId ARTHROPOD_DAMAGE = AttributeId.parse("U33");
    private static final AttributeId ENDER_DAMAGE = AttributeId.parse("U36");
    private static final AttributeId MAGMATIC_DAMAGE = AttributeId.parse("U38");
    private static final AttributeId HUMANOID_DAMAGE = AttributeId.parse("R18");
    private static final AttributeId INFERNAL_DAMAGE = AttributeId.parse("E18");

    private static final AttributeId ARTHROPOD_DEFENSE = AttributeId.parse("C15");
    private static final AttributeId UNDEAD_DEFENSE = AttributeId.parse("C18");
    private static final AttributeId ENDER_DEFENSE = AttributeId.parse("C21");
    private static final AttributeId INFERNAL_DEFENSE = AttributeId.parse("C30");
    private static final AttributeId MYTHOLOGICAL_DEFENSE = AttributeId.parse("C39");

    private static final AttributeId COMMON_KARMA = AttributeId.parse("C35");
    private static final AttributeId UNCOMMON_KARMA = AttributeId.parse("U8");
    private static final AttributeId RARE_KARMA = AttributeId.parse("R8");
    private static final AttributeId EPIC_KARMA = AttributeId.parse("E5");
    private static final AttributeId LEGENDARY_KARMA = AttributeId.parse("L29");

    private AttributeEffectService() {
    }

    public static ItemStatistics statistics(SkyBlockPlayer player) {
        return statistics(player.getHuntingData(), AttributeRegistry::get);
    }

    static ItemStatistics statistics(DatapointHunting.HuntingData data,
                                     Function<String, AttributeDefinition> definitions) {
        ItemStatistics result = ItemStatistics.empty();
        if (data.getSyphoned().isEmpty()) return result;

        double echo = Double.NaN;
        double elementalBonus = 0;

        for (Map.Entry<String, Integer> entry : data.getSyphoned().entrySet()) {
            if (entry.getValue() <= 0) continue;

            AttributeDefinition definition = definitions.apply(entry.getKey());
            if (definition == null) continue;

            String key = definition.id().toString();
            if (!entry.getKey().equals(key)) continue;

            int level = definition.rarity().levelFor(entry.getValue());
            if (level == 0 || data.getDisabled().contains(key)) continue;

            double multiplier = 1;
            if (definition.hasFamily(AttributeFamily.ELEMENTAL) && !definition.id().equals(ELEMENTAL_BONUS)) {
                if (Double.isNaN(echo)) {
                    echo = 1 + rawValue(data, ECHO, definitions) / 100D;
                    elementalBonus = rawValue(data, ELEMENTAL_BONUS, definitions) / 100D;
                }
                multiplier = 1 + elementalBonus * echo;
            }
            result = applyEffects(result, definition, level, multiplier);
        }
        return result;
    }

    public static double value(DatapointHunting.HuntingData data, AttributeId id) {
        AttributeDefinition definition = AttributeRegistry.get(id);
        if (definition == null) return 0;

        String key = id.toString();
        int level = definition.rarity().levelFor(data.syphoned(key));
        if (level == 0 || data.getDisabled().contains(key)) return 0;

        double echo = 1 + rawValue(data, ECHO) / 100D;
        double elementalBonus = rawValue(data, ELEMENTAL_BONUS) / 100D;
        return definition.effects().stream().findFirst()
                .map(effect -> effect.atLevel(level) * familyMultiplier(definition, echo, elementalBonus))
                .orElse(0D);
    }

    public static double outgoingDamageMultiplier(DatapointHunting.HuntingData data, SkyBlockMob mob, boolean ranged) {
        double percentage = value(data, ranged ? RANGED_DAMAGE : MELEE_DAMAGE);
        percentage += typedValue(data, mob, MobType.SKELETAL, SKELETAL_DAMAGE);
        percentage += typedValue(data, mob, MobType.UNDEAD, UNDEAD_DAMAGE);
        percentage += typedValue(data, mob, MobType.WOODLAND, WOODLAND_DAMAGE);
        percentage += typedValue(data, mob, MobType.ARTHROPOD, ARTHROPOD_DAMAGE);
        percentage += typedValue(data, mob, MobType.ENDER, ENDER_DAMAGE);
        percentage += typedValue(data, mob, MobType.MAGMATIC, MAGMATIC_DAMAGE);
        percentage += typedValue(data, mob, MobType.HUMANOID, HUMANOID_DAMAGE);
        percentage += typedValue(data, mob, MobType.INFERNAL, INFERNAL_DAMAGE);
        return 1D + percentage / 100D;
    }

    public static double resistanceDefense(DatapointHunting.HuntingData data, SkyBlockMob mob) {
        return typedValue(data, mob, MobType.ARTHROPOD, ARTHROPOD_DEFENSE)
                + typedValue(data, mob, MobType.UNDEAD, UNDEAD_DEFENSE)
                + typedValue(data, mob, MobType.ENDER, ENDER_DEFENSE)
                + typedValue(data, mob, MobType.INFERNAL, INFERNAL_DEFENSE)
                + typedValue(data, mob, MobType.MYTHOLOGICAL, MYTHOLOGICAL_DEFENSE);
    }

    public static double hunterFortune(SkyBlockPlayer player, AttributeDefinition hunted) {
        double fortune = player.getStatistics().allStatistics().getOverall(ItemStatistic.HUNTER_FORTUNE);
        AttributeId karma = switch (hunted.rarity()) {
            case COMMON -> COMMON_KARMA;
            case UNCOMMON -> UNCOMMON_KARMA;
            case RARE -> RARE_KARMA;
            case EPIC -> EPIC_KARMA;
            case LEGENDARY -> LEGENDARY_KARMA;
        };
        return fortune + value(player.getHuntingData(), karma);
    }

    public static int fortunateAmount(SkyBlockPlayer player, AttributeDefinition hunted) {
        double fortune = hunterFortune(player, hunted);
        int amount = 1 + (int) (fortune / 100D);
        return ThreadLocalRandom.current().nextDouble() < fortune % 100D / 100D ? amount + 1 : amount;
    }

    private static double typedValue(DatapointHunting.HuntingData data, SkyBlockMob mob, MobType type, AttributeId id) {
        return mob.getMobTypes().contains(type) ? value(data, id) : 0;
    }

    static ItemStatistics applyEffects(ItemStatistics result, AttributeDefinition definition, int level, double multiplier) {
        for (AttributeEffect effect : definition.effects()) {
            if (effect.statistic() == null || !active(effect.condition())) continue;
            double value = effect.atLevel(level) * multiplier;
            result = effect.type() == EffectType.PERCENTAGE_STATISTIC_PER_LEVEL
                    ? result.addAdditive(effect.statistic(), value / 100D)
                    : result.addBase(effect.statistic(), value);
        }
        return result;
    }

    static double familyMultiplier(AttributeDefinition definition, double echo, double elementalBonus) {
        if (definition.hasFamily(AttributeFamily.ELEMENTAL) && !definition.id().equals(ELEMENTAL_BONUS)) {
            return 1 + elementalBonus * echo;
        }
        return 1;
    }

    private static boolean active(Condition condition) {
        if (condition == Condition.ALWAYS) return true;
        if (condition == Condition.DAY) {
            int hour = SkyBlockCalendar.getHour();
            return hour >= 6 && hour < 20;
        }
        if (condition == Condition.NIGHT) {
            int hour = SkyBlockCalendar.getHour();
            return hour < 6 || hour >= 20;
        }

        ServerType server = HypixelConst.getTypeLoader().getType();
        return switch (condition) {
            case FORAGING_ISLAND -> server == ServerType.SKYBLOCK_GALATEA || server == ServerType.SKYBLOCK_THE_PARK;
            case FISHING_ISLAND ->
                    server == ServerType.SKYBLOCK_BACKWATER_BAYOU || server == ServerType.SKYBLOCK_JERRYS_WORKSHOP;
            case COMBAT_ISLAND ->
                    server == ServerType.SKYBLOCK_SPIDERS_DEN || server == ServerType.SKYBLOCK_THE_END || server == ServerType.SKYBLOCK_CRIMSON_ISLE;
            case END -> server == ServerType.SKYBLOCK_THE_END;
            default -> true;
        };
    }

    private static double rawValue(DatapointHunting.HuntingData data, AttributeId id) {
        return rawValue(data, id, AttributeRegistry::get);
    }

    private static double rawValue(DatapointHunting.HuntingData data, AttributeId id,
                                   Function<String, AttributeDefinition> definitions) {
        AttributeDefinition definition = definitions.apply(id.toString());
        if (definition == null) return 0;

        String key = id.toString();
        int level = definition.rarity().levelFor(data.syphoned(key));
        if (level == 0 || data.getDisabled().contains(key)) return 0;
        return definition.effects().stream().findFirst().map(effect -> effect.atLevel(level)).orElse(0D);
    }
}
