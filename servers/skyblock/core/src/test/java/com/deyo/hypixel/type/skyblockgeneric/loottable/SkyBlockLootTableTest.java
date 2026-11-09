package com.deyo.hypixel.type.skyblockgeneric.loottable;

import com.deyo.hypixel.commons.skyblock.item.ItemType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkyBlockLootTableTest {

    @Test
    void pickOneDoesNotMutateAnImmutableLootTable() {
        SkyBlockLootTable table = table(List.of(
                new SkyBlockLootTable.LootRecord(ItemType.ROTTEN_FLESH, 1, 100)),
                SkyBlockLootTable.CalculationMode.PICK_ONE);

        Map<ItemType, SkyBlockLootTable.LootRecord> loot = table.runChances(null);

        assertEquals(1, loot.size());
        assertTrue(loot.containsKey(ItemType.ROTTEN_FLESH));
    }

    @Test
    void pickOneWithoutAPlayerDoesNotMutateAnImmutableLootTable() {
        SkyBlockLootTable table = table(List.of(
                new SkyBlockLootTable.LootRecord(ItemType.ROTTEN_FLESH, 1, 100)),
                SkyBlockLootTable.CalculationMode.PICK_ONE);

        Map<ItemType, SkyBlockLootTable.LootRecord> loot = table.runChancesNoPlayer();

        assertEquals(1, loot.size());
        assertTrue(loot.containsKey(ItemType.ROTTEN_FLESH));
    }

    @Test
    void pickOneOnlySelectsRecordsThatPassTheirPredicate() {
        SkyBlockLootTable.LootRecord skipped = new SkyBlockLootTable.LootRecord(
                ItemType.LAPIS_ARMOR_BOOTS, 1, 100, player -> false);
        SkyBlockLootTable.LootRecord selected = new SkyBlockLootTable.LootRecord(
                ItemType.ROTTEN_FLESH, 1, 1, player -> true);
        SkyBlockLootTable table = table(List.of(skipped, selected),
                SkyBlockLootTable.CalculationMode.PICK_ONE);

        Map<ItemType, SkyBlockLootTable.LootRecord> loot = table.runChances(null);

        assertEquals(1, loot.size());
        assertTrue(loot.containsKey(ItemType.ROTTEN_FLESH));
    }

    @Test
    void pickOneWithZeroTotalChanceDropsNothing() {
        SkyBlockLootTable table = table(List.of(
                new SkyBlockLootTable.LootRecord(ItemType.ROTTEN_FLESH, 1, 0)),
                SkyBlockLootTable.CalculationMode.PICK_ONE);

        assertTrue(table.runChances(null).isEmpty());
        assertTrue(table.runChancesNoPlayer().isEmpty());
    }

    private static SkyBlockLootTable table(List<SkyBlockLootTable.LootRecord> records,
                                           SkyBlockLootTable.CalculationMode mode) {
        return new SkyBlockLootTable() {
            @Override
            public List<LootRecord> getLootTable() {
                return records;
            }

            @Override
            public CalculationMode getCalculationMode() {
                return mode;
            }
        };
    }
}
