package com.deyo.hypixel.type.skyblockgeneric.slayer;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SlayerDefinitionTest {

    @Test
    void orderedTiersAreSortedByTierNumber() {
        SlayerDefinition definition = new SlayerDefinition(
                SlayerType.REVENANT_HORROR,
                true,
                Optional.empty(),
                List.of(),
                List.of(),
                Map.of(
                        SlayerTier.III, tier(SlayerTier.III),
                        SlayerTier.I, tier(SlayerTier.I),
                        SlayerTier.V, tier(SlayerTier.V),
                        SlayerTier.II, tier(SlayerTier.II),
                        SlayerTier.IV, tier(SlayerTier.IV)
                )
        );

        assertEquals(
                List.of(SlayerTier.I, SlayerTier.II, SlayerTier.III, SlayerTier.IV, SlayerTier.V),
                definition.orderedTiers().stream().map(SlayerTierDefinition::tier).toList()
        );
    }

    private static SlayerTierDefinition tier(SlayerTier tier) {
        return new SlayerTierDefinition(tier, 0, 0, 0, 0, 0, 0, 0, 0, Optional.empty());
    }
}
