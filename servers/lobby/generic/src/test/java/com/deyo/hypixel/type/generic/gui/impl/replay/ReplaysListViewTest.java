package com.deyo.hypixel.type.generic.gui.impl.replay;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReplaysListViewTest {

    @Test
    void formatsReplayStartTimeAsMinuteTimestamp() {
        String formatted = ReplaysListView.formatStartTime(1_700_000_000_000L);

        assertTrue(formatted.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}"), formatted);
    }

    @Test
    void showReplaysOnlyFiltersEntriesWithoutAReplay() {
        ReplaysListView view = new ReplaysListView(_ -> {});
        ReplaysListView.State state = new ReplaysListView.State(List.of(), 0, true);

        assertTrue(view.shouldFilterFromSearch(state, entry(null)));
        assertFalse(view.shouldFilterFromSearch(state, entry(UUID.randomUUID())));
    }

    @Test
    void disablingTheFilterKeepsEveryGame() {
        ReplaysListView view = new ReplaysListView(_ -> {});
        ReplaysListView.State state = new ReplaysListView.State(List.of(), 0, false);

        assertFalse(view.shouldFilterFromSearch(state, entry(null)));
    }

    @Test
    void pageAndItemUpdatesKeepTheFilterFlag() {
        ReplaysListView.State state = new ReplaysListView.State(List.of(), 2, true);

        assertTrue(state.withPage(3).showReplaysOnly());
        assertTrue(state.withItems(List.of()).showReplaysOnly());
    }

    private static ReplayEntry entry(UUID replayId) {
        return ReplayEntry.builder().replayId(replayId).build();
    }
}
