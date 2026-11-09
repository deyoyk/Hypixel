package com.deyo.hypixel.type.replayviewer.view;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GUIPlayersStateTest {

    @Test
    void pageChangesPreserveItems() {
        GUIPlayers.State state = new GUIPlayers.State(List.of(), 2);

        assertEquals(2, state.page());
        assertEquals(3, state.withPage(3).page());
        assertTrue(state.withItems(List.of()).items().isEmpty());
    }
}
