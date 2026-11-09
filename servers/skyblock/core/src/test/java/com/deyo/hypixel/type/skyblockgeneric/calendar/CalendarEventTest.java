package com.deyo.hypixel.type.skyblockgeneric.calendar;

import net.minestom.server.MinecraftServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalendarEventTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @Test
    void darkAuctionIsActiveOnlyDuringItsDuration() {
        long start = 3L * SkyBlockCalendar.DAY;

        assertTrue(CalendarEvent.getCurrentEvents(start).contains(CalendarEvent.DARK_AUCTION));
        assertFalse(CalendarEvent.getCurrentEvents(start - 1).contains(CalendarEvent.DARK_AUCTION));
        assertFalse(CalendarEvent.getCurrentEvents(start + 6_000).contains(CalendarEvent.DARK_AUCTION));
    }

    @Test
    void newYearEventIsActiveOnlyDuringItsDuration() {
        assertTrue(CalendarEvent.getCurrentEvents(10).contains(CalendarEvent.NEW_YEAR));
        assertFalse(CalendarEvent.getCurrentEvents(10 + 72_000).contains(CalendarEvent.NEW_YEAR));
    }

    @Test
    void eventStartTimesAreSorted() {
        for (CalendarEvent event : CalendarEvent.getAllEvents()) {
            List<Long> times = event.times();
            for (int index = 1; index < times.size(); index++) {
                assertTrue(times.get(index - 1) <= times.get(index),
                        event.id() + " has unsorted start times");
            }
        }
    }
}
