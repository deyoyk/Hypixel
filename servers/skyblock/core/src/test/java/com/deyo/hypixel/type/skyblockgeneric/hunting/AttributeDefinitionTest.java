package com.deyo.hypixel.type.skyblockgeneric.hunting;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeDefinition.AttributeCategory;
import static com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeDefinition.AttributeFamily;
import static com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeDefinition.AttributeSkill;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AttributeDefinitionTest {

    @Test
    void keepsMultipleFamilies() {
        AttributeDefinition definition = definition(List.of(AttributeFamily.AMPHIBIAN, AttributeFamily.EEL));

        assertTrue(definition.hasFamily(AttributeFamily.AMPHIBIAN));
        assertTrue(definition.hasFamily(AttributeFamily.EEL));
        assertFalse(definition.hasFamily(AttributeFamily.ELEMENTAL));
        assertEquals(List.of(AttributeFamily.AMPHIBIAN, AttributeFamily.EEL), definition.families());
    }

    @Test
    void defaultsMissingFamiliesToNone() {
        assertEquals(List.of(AttributeFamily.NONE), definition(null).families());
        assertEquals(List.of(AttributeFamily.NONE), definition(List.of()).families());
    }

    @Test
    void familiesAreImmutable() {
        AttributeDefinition definition = definition(List.of(AttributeFamily.AMPHIBIAN));

        assertThrows(UnsupportedOperationException.class, () -> definition.families().add(AttributeFamily.EEL));
    }

    @Test
    void fusionRequirementUsesAnyMatchingFamily() {
        assertEquals(2, AttributeFusionService.required(
                definition(List.of(AttributeFamily.AMPHIBIAN, AttributeFamily.EEL))));
        assertEquals(2, AttributeFusionService.required(definition(List.of(AttributeFamily.REPTILE))));
        assertEquals(2, AttributeFusionService.required(definition(List.of(AttributeFamily.ELEMENTAL))));
        assertEquals(5, AttributeFusionService.required(definition(List.of(AttributeFamily.NONE))));
        assertEquals(1, AttributeFusionService.required(
                new AttributeDefinition(new AttributeId(AttributeRarity.LEGENDARY, 11), "Chameleon", "Charmed",
                        AttributeRarity.LEGENDARY, AttributeCategory.WATER, List.of(AttributeFamily.NONE),
                        AttributeSkill.HUNTING, List.of(), List.of(), "", List.of())));
    }

    private static AttributeDefinition definition(List<AttributeFamily> families) {
        return new AttributeDefinition(new AttributeId(AttributeRarity.LEGENDARY, 11), "Naga", "Charmed",
                AttributeRarity.LEGENDARY, AttributeCategory.WATER, families, AttributeSkill.HUNTING,
                List.of(), List.of(), "", List.of());
    }
}
