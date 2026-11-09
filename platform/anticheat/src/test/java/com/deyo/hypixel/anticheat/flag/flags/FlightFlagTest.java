package com.deyo.hypixel.anticheat.flag.flags;

import com.deyo.hypixel.anticheat.engine.PlayerTickInformation;
import com.deyo.hypixel.anticheat.math.Pos;
import com.deyo.hypixel.anticheat.math.Vel;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlightFlagTest {

    @Test
    void countsMoreThanTenConsecutiveAirTicks() {
        assertEquals(16, FlightFlag.countAirTicks(ticks(16, false)));
    }

    @Test
    void stopsCountingAtTheLastGroundTick() {
        List<PlayerTickInformation> ticks = new ArrayList<>();
        ticks.addAll(ticks(5, false));
        ticks.add(tick(0, true));
        ticks.addAll(ticks(3, false));

        assertEquals(3, FlightFlag.countAirTicks(ticks));
    }

    @Test
    void emptyHistoryHasNoAirTicks() {
        assertEquals(0, FlightFlag.countAirTicks(List.of()));
    }

    @Test
    void averagesTheMostRecentVelocities() {
        List<PlayerTickInformation> ticks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ticks.add(tick(i, false));
        }

        assertEquals(3.0, FlightFlag.calculateAverageYVelocity(ticks, 3));
    }

    private static List<PlayerTickInformation> ticks(int count, boolean onGround) {
        List<PlayerTickInformation> ticks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ticks.add(tick(0, onGround));
        }
        return ticks;
    }

    private static PlayerTickInformation tick(double yVelocity, boolean onGround) {
        return new PlayerTickInformation(new Pos(0, 0, 0), new Vel(0, yVelocity, 0), onGround);
    }
}
