package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.centauri;

import com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeDefinition;
import com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeId;
import com.deyo.hypixel.type.skyblockgeneric.hunting.AttributeRarity;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GUICentauriToyBoxTest {

    @Test
    void grantShardsAppliesTheAmountToEveryDefinition() {
        AttributeDefinition first = definition(1);
        AttributeDefinition second = definition(2);
        Map<AttributeId, Integer> grants = new LinkedHashMap<>();

        int granted = GUICentauriToyBox.grantShards(List.of(first, second), 10, grants::put);

        assertEquals(2, granted);
        assertEquals(10, grants.get(first.id()));
        assertEquals(10, grants.get(second.id()));
    }

    private static AttributeDefinition definition(int number) {
        return new AttributeDefinition(
                new AttributeId(AttributeRarity.COMMON, number),
                "Shard " + number,
                "Shard " + number,
                AttributeRarity.COMMON,
                AttributeDefinition.AttributeCategory.FOREST,
                List.of(AttributeDefinition.AttributeFamily.NONE),
                AttributeDefinition.AttributeSkill.HUNTING,
                List.of(),
                List.of(),
                "",
                List.of()
        );
    }
}
