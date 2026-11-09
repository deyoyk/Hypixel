package com.deyo.hypixel.type.bedwarslobby.gui;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GUIMapSelectionOrderTest {

    @Test
    void favoriteMapsAreOrderedFirstAndKeepAlphabeticalOrder() {
        List<String> ordered = GUIMapSelection.orderMaps(
                List.of("Zeta", "Alpha", "Bravo", "Charlie"),
                Set.of("bravo")
        );

        assertEquals(List.of("Bravo", "Alpha", "Charlie", "Zeta"), ordered);
    }

    @Test
    void mapIdsMatchTheFavouriteStorageFormat() {
        assertEquals("map_name", GUIMapSelection.mapId("Map Name"));
        assertEquals("map_name", GUIMapSelection.mapId("  Map--Name!!  "));
        assertEquals("map_name", GUIMapSelection.mapId("__Map_Name__"));
    }

    @Test
    void mapIdsFallBackWhenThereAreNoAlphanumericCharacters() {
        assertEquals("___", GUIMapSelection.mapId("___"));
    }
}
