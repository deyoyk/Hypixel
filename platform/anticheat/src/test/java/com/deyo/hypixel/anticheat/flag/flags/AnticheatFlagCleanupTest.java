package com.deyo.hypixel.anticheat.flag.flags;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;

class AnticheatFlagCleanupTest {

    @Test
    void disconnectingPlayerIsRemovedFromEveryFlagStateMap() throws Exception {
        UUID uuid = UUID.randomUUID();

        List<Map<UUID, Object>> maps = List.of(
                map(TimerFlag.class, "lastPacketTimes"),
                map(TimerFlag.class, "packetCounts"),
                map(TimerFlag.class, "intervalTotals"),
                map(VelocityFlag.class, "expectedKnockback"),
                map(KillAuraFlag.class, "attackData"),
                map(AimFlag.class, "rotationData"),
                map(AutoClickerFlag.class, "clickData"),
                map(BadPacketsFlag.class, "packetData"),
                map(PredictionFlag.class, "currentTransactions")
        );

        for (Map<UUID, Object> state : maps) {
            state.put(uuid, new Object());
        }

        TimerFlag.clearPlayer(uuid);
        VelocityFlag.clearPlayer(uuid);
        KillAuraFlag.clearPlayer(uuid);
        AimFlag.clearPlayer(uuid);
        AutoClickerFlag.clearPlayer(uuid);
        BadPacketsFlag.clearPlayer(uuid);
        PredictionFlag.clearPlayer(uuid);

        for (Map<UUID, Object> state : maps) {
            assertFalse(state.containsKey(uuid));
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Map<UUID, Object> map(Class<?> owner, String fieldName) throws Exception {
        Field field = owner.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (Map) field.get(null);
    }
}
