package com.deyo.hypixel.type.skyblockgeneric.gui.inventories.rabbits;

import com.deyo.hypixel.type.skyblockgeneric.rabbits.ChocolateRabbit;
import com.deyo.hypixel.type.skyblockgeneric.region.IslandType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GUIHoppityCollectionTest {

    @Test
    void locationFilterCyclesThroughLocationsAndBackToAll() {
        IslandType first = GUIHoppityCollection.cycleLocation(null, false);
        assertNotNull(first);
        assertEquals(first, GUIHoppityCollection.cycleLocation(null, false));
        assertNull(GUIHoppityCollection.cycleLocation(first, true));

        IslandType last = GUIHoppityCollection.cycleLocation(null, true);
        assertNotNull(last);
        assertNull(GUIHoppityCollection.cycleLocation(last, false));

        IslandType second = GUIHoppityCollection.cycleLocation(first, false);
        assertEquals(first, GUIHoppityCollection.cycleLocation(second, true));
    }

    @Test
    void locationFilterOnlyReturnsRabbitsFromSelectedIsland() {
        IslandType location = firstResidentLocation();

        List<ChocolateRabbit> filtered = GUIHoppityCollection.getFilteredAndSortedRabbits(
                Set.of(), GUIHoppityCollection.SortType.A_TO_Z,
                GUIHoppityCollection.FilterType.NONE, location);

        assertFalse(filtered.isEmpty());
        assertTrue(filtered.stream().allMatch(rabbit -> rabbit.getLocation() == location));
        assertTrue(filtered.size() < ChocolateRabbit.values().length);

        for (int i = 1; i < filtered.size(); i++) {
            assertTrue(Comparator.comparing(ChocolateRabbit::getDisplayName)
                    .compare(filtered.get(i - 1), filtered.get(i)) <= 0);
        }
    }

    @Test
    void locationFilterCombinesWithFoundFilter() {
        IslandType location = firstResidentLocation();
        List<ChocolateRabbit> allAtLocation = GUIHoppityCollection.getFilteredAndSortedRabbits(
                Set.of(), GUIHoppityCollection.SortType.A_TO_Z,
                GUIHoppityCollection.FilterType.NONE, location);
        ChocolateRabbit foundRabbit = allAtLocation.getFirst();

        List<ChocolateRabbit> found = GUIHoppityCollection.getFilteredAndSortedRabbits(
                Set.of(foundRabbit), GUIHoppityCollection.SortType.A_TO_Z,
                GUIHoppityCollection.FilterType.FOUND, location);
        assertEquals(List.of(foundRabbit), found);

        List<ChocolateRabbit> notFound = GUIHoppityCollection.getFilteredAndSortedRabbits(
                Set.of(foundRabbit), GUIHoppityCollection.SortType.A_TO_Z,
                GUIHoppityCollection.FilterType.NOT_FOUND, location);
        assertFalse(notFound.contains(foundRabbit));
        assertTrue(notFound.stream().allMatch(rabbit -> rabbit.getLocation() == location));
    }

    private static IslandType firstResidentLocation() {
        return Arrays.stream(ChocolateRabbit.values())
                .map(ChocolateRabbit::getLocation)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow();
    }
}
