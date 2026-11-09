package com.deyo.hypixel.type.skyblockgeneric.calendar;

import net.minestom.server.MinecraftServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkyBlockCalendarNextEventTest {

    @BeforeAll
    static void initServer() {
        MinecraftServer.init();
    }

    @AfterEach
    void resetElapsed() {
        SkyBlockCalendar.setElapsed(0L);
    }

    @Test
    void ticksUntilDarkAuctionSkipsTheCurrentStartTime() {
        assertEquals(3L * SkyBlockCalendar.DAY,
                SkyBlockCalendar.ticksUntilEvent(CalendarEvent.DARK_AUCTION));
    }

    @Test
    void upcomingEventsAreInTheFuture() {
        Map<SkyBlockCalendar.EventInfo, CalendarEvent> events =
                SkyBlockCalendar.getEventsWithDurationUntil(3);

        assertEquals(3, events.size());
        assertTrue(events.keySet().stream().allMatch(info -> info.timeUntilBegin() > 0));
    }

    @Test
    void ticksUntilEventUsesTheCurrentYear() {
        SkyBlockCalendar.setElapsed(SkyBlockCalendar.YEAR + 10L);

        assertEquals(3L * SkyBlockCalendar.DAY - 10L,
                SkyBlockCalendar.ticksUntilEvent(CalendarEvent.DARK_AUCTION));
    }
}
