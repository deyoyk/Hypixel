package com.deyo.hypixel.type.skyblockgeneric.hunting;

import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistic;
import com.deyo.hypixel.commons.skyblock.statistics.ItemStatistics;
import com.deyo.hypixel.type.skyblockgeneric.data.datapoints.DatapointHunting;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeDefinition.AttributeCategory;
import static com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeDefinition.AttributeEffect;
import static com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeDefinition.AttributeFamily;
import static com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeDefinition.AttributeSkill;
import static com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeDefinition.Condition;
import static com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeDefinition.EffectType;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AttributeEffectServiceTest {

    @Test
    void appliesFlatAndPercentageEffectsAtTheGivenLevel() {
        AttributeDefinition definition = definition(
                new AttributeId(AttributeRarity.COMMON, 1),
                List.of(AttributeFamily.NONE),
                List.of(
                        new AttributeEffect(EffectType.STATISTIC_PER_LEVEL, ItemStatistic.HEALTH,
                                2, 20, Condition.ALWAYS, null, ""),
                        new AttributeEffect(EffectType.PERCENTAGE_STATISTIC_PER_LEVEL, ItemStatistic.INTELLIGENCE,
                                5, 5, Condition.ALWAYS, null, ""),
                        new AttributeEffect(EffectType.STATISTIC_PER_LEVEL, null,
                                100, 100, Condition.ALWAYS, null, "")
                ));

        ItemStatistics result = AttributeEffectService.applyEffects(
                ItemStatistics.empty(), definition, 4, 1.5);

        assertEquals(12D, result.getBase(ItemStatistic.HEALTH));
        assertEquals(1.075D, result.getAdditive(ItemStatistic.INTELLIGENCE));
    }

    @Test
    void elementalAttributesUseTheEchoScaledBonus() {
        AttributeDefinition elemental = definition(
                new AttributeId(AttributeRarity.RARE, 2),
                List.of(AttributeFamily.ELEMENTAL),
                List.of());
        AttributeDefinition plain = definition(
                new AttributeId(AttributeRarity.RARE, 3),
                List.of(AttributeFamily.NONE),
                List.of());
        AttributeDefinition bonusItself = definition(
                new AttributeId(AttributeRarity.LEGENDARY, 44),
                List.of(AttributeFamily.ELEMENTAL),
                List.of());

        assertEquals(1.15D, AttributeEffectService.familyMultiplier(elemental, 1.5D, 0.1D));
        assertEquals(1D, AttributeEffectService.familyMultiplier(plain, 1.5D, 0.1D));
        assertEquals(1D, AttributeEffectService.familyMultiplier(bonusItself, 1.5D, 0.1D));
    }

    @Test
    void statisticsOnlyAppliesEnabledAttributes() {
        AttributeDefinition active = definition(
                new AttributeId(AttributeRarity.COMMON, 1),
                List.of(AttributeFamily.NONE),
                List.of(new AttributeEffect(EffectType.STATISTIC_PER_LEVEL, ItemStatistic.HEALTH,
                        10, 10, Condition.ALWAYS, null, "")));
        AttributeDefinition disabled = definition(
                new AttributeId(AttributeRarity.COMMON, 2),
                List.of(AttributeFamily.NONE),
                List.of(new AttributeEffect(EffectType.STATISTIC_PER_LEVEL, ItemStatistic.HEALTH,
                        50, 50, Condition.ALWAYS, null, "")));

        DatapointHunting.HuntingData data = new DatapointHunting.HuntingData(
                Map.of(), Map.of("C1", 4, "c1", 4, "C2", 4), Set.of("C2"), Map.of(), List.of());

        ItemStatistics result = AttributeEffectService.statistics(
                data, id -> id.equalsIgnoreCase("C1") ? active : id.equalsIgnoreCase("C2") ? disabled : null);

        assertEquals(10D, result.getBase(ItemStatistic.HEALTH));
    }

    @Test
    void elementalAttributesUseTheEchoScaledBonusFromHuntingData() {
        AttributeDefinition elemental = definition(
                new AttributeId(AttributeRarity.RARE, 2),
                List.of(AttributeFamily.ELEMENTAL),
                List.of(new AttributeEffect(EffectType.STATISTIC_PER_LEVEL, ItemStatistic.HEALTH,
                        10, 10, Condition.ALWAYS, null, "")));
        AttributeDefinition plain = definition(
                new AttributeId(AttributeRarity.RARE, 3),
                List.of(AttributeFamily.NONE),
                List.of(new AttributeEffect(EffectType.STATISTIC_PER_LEVEL, ItemStatistic.HEALTH,
                        10, 10, Condition.ALWAYS, null, "")));
        AttributeDefinition echo = definition(
                new AttributeId(AttributeRarity.LEGENDARY, 6),
                List.of(AttributeFamily.NONE),
                List.of(new AttributeEffect(EffectType.STATISTIC_PER_LEVEL, null,
                        100, 100, Condition.ALWAYS, null, "")));
        AttributeDefinition bonus = definition(
                new AttributeId(AttributeRarity.LEGENDARY, 44),
                List.of(AttributeFamily.ELEMENTAL),
                List.of(new AttributeEffect(EffectType.STATISTIC_PER_LEVEL, null,
                        10, 10, Condition.ALWAYS, null, "")));

        DatapointHunting.HuntingData data = new DatapointHunting.HuntingData(
                Map.of(), Map.of("R2", 3, "R3", 3, "L6", 3, "L44", 1), Set.of(), Map.of(), List.of());

        ItemStatistics result = AttributeEffectService.statistics(
                data, Map.of("R2", elemental, "R3", plain, "L6", echo, "L44", bonus)::get);

        assertEquals(22D, result.getBase(ItemStatistic.HEALTH));
    }

    private static AttributeDefinition definition(AttributeId id, List<AttributeFamily> families,
                                                   List<AttributeEffect> effects) {
        return new AttributeDefinition(id, "Shard", "Shard", id.rarity(), AttributeCategory.FOREST,
                families, AttributeSkill.HUNTING, effects, List.of(), "", List.of());
    }
}
