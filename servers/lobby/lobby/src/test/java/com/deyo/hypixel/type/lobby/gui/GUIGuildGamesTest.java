package com.deyo.hypixel.type.lobby.gui;

import com.deyo.hypixel.type.lobby.game.GameType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GUIGuildGamesTest {

    @Test
    void togglingAddsAndRemovesTheSelectedGame() {
        List<String> selected = GUIGuildGames.toggled(List.of(), GameType.BED_WARS);

        assertEquals(List.of("BED_WARS"), selected);
        assertEquals(List.of("BED_WARS", "SKYWARS"),
                GUIGuildGames.toggled(selected, GameType.SKYWARS));
        assertEquals(List.of("SKYWARS"),
                GUIGuildGames.toggled(List.of("BED_WARS", "SKYWARS"), GameType.BED_WARS));
    }

    @Test
    void togglingLeavesTheOriginalListUntouched() {
        List<String> original = List.of("BED_WARS");

        GUIGuildGames.toggled(original, GameType.SKYWARS);

        assertEquals(List.of("BED_WARS"), original);
    }
}
