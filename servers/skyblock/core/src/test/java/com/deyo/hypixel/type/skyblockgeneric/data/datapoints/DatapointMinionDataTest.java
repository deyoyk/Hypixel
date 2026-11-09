package com.deyo.hypixel.type.skyblockgeneric.data.datapoints;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatapointMinionDataTest {

    @Test
    void slotThresholdsUseTheHighestReachedTier() {
        assertEquals(5, data(0).getSlots());
        assertEquals(5, data(1).getSlots());
        assertEquals(5, data(4).getSlots());
        assertEquals(6, data(5).getSlots());
        assertEquals(6, data(10).getSlots());
        assertEquals(7, data(11).getSlots());
    }

    @Test
    void additionalUniqueMinionsKeepTheHighestSlotCount() {
        assertEquals(7, data(12).getSlots());
        assertEquals(7, data(52).getSlots());
    }

    @Test
    void serializationRoundTripsEmptyAndMultipleTiers() throws Exception {
        DatapointMinionData.ProfileMinionData original = new DatapointMinionData.ProfileMinionData(
                new ArrayList<>(List.of(
                        Map.entry("EMPTY", new ArrayList<>()),
                        Map.entry("COBBLESTONE", new ArrayList<>(List.of(1, 2)))
                )));
        DatapointMinionData datapoint = new DatapointMinionData("test", original);

        DatapointMinionData.ProfileMinionData restored =
                datapoint.getSerializer().deserialize(datapoint.getSerializedValue());

        assertEquals(2, restored.craftedMinions().size());
        Map<String, List<Integer>> restoredTiers = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : restored.craftedMinions()) {
            restoredTiers.put(entry.getKey(), entry.getValue());
        }
        assertEquals(List.of(), restoredTiers.get("EMPTY"));
        assertEquals(List.of(1, 2), restoredTiers.get("COBBLESTONE"));
    }

    @Test
    void duplicateMinionEntriesCountOnlyOnce() {
        DatapointMinionData.ProfileMinionData data = new DatapointMinionData.ProfileMinionData(List.of(
                Map.entry("COBBLESTONE", List.of(1)),
                Map.entry("COBBLESTONE", List.of(2))
        ));

        assertEquals(1, data.getUniqueMinions());
        assertEquals(5, data.getSlots());
    }

    private static DatapointMinionData.ProfileMinionData data(int uniqueMinions) {
        List<Map.Entry<String, List<Integer>>> entries = new ArrayList<>();
        for (int i = 0; i < uniqueMinions; i++) {
            entries.add(Map.entry("MINION_" + i, List.of(1)));
        }
        return new DatapointMinionData.ProfileMinionData(entries);
    }
}
