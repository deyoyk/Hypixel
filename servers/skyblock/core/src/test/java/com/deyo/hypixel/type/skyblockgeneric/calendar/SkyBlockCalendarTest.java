package com.deyo.hypixel.type.skyblockgeneric.calendar;

import net.minestom.server.MinecraftServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkyBlockCalendarTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @AfterEach
    void resetElapsed() {
        SkyBlockCalendar.setElapsed(0L);
    }

    @Test
    void bothEventQueriesAgreeWhenNothingIsExcluded() {
        SkyBlockCalendar.setElapsed(0L);

        Map<SkyBlockCalendar.EventInfo, CalendarEvent> all =
                SkyBlockCalendar.getEventsWithDurationUntil(5);
        Map<SkyBlockCalendar.EventInfo, CalendarEvent> skipNone =
                SkyBlockCalendar.getEventsWithDurationUntilSkipSpecific(5, List.of());

        assertFalse(all.isEmpty());
        assertEquals(all, skipNone);
    }

    @Test
    void excludedEventsAreNotReturned() {
        SkyBlockCalendar.setElapsed(0L);

        Map<SkyBlockCalendar.EventInfo, CalendarEvent> all =
                SkyBlockCalendar.getEventsWithDurationUntil(10);
        CalendarEvent excluded = all.values().iterator().next();

        Map<SkyBlockCalendar.EventInfo, CalendarEvent> without =
                SkyBlockCalendar.getEventsWithDurationUntilSkipSpecific(10, List.of(excluded));

        assertFalse(without.containsValue(excluded));
    }

    @Test
    void displayPadsMinutesAndShowsTheDayPeriod() {
        assertTrue(SkyBlockCalendar.getDisplay(6_000L).serialize().contains("6:00am"));
        assertTrue(SkyBlockCalendar.getDisplay(6_084L).serialize().contains("6:05am"));
        assertTrue(SkyBlockCalendar.getDisplay(6_000L).serialize().contains("☀"));
        assertTrue(SkyBlockCalendar.getDisplay(20_000L).serialize().contains("8:00pm"));
        assertTrue(SkyBlockCalendar.getDisplay(20_000L).serialize().contains("☽"));
    }
}
