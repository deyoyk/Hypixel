package com.deyo.hypixel.type.replayviewer.playback;

import com.deyo.hypixel.commons.replay.protocol.ReplayDataReader;
import com.deyo.hypixel.commons.replay.protocol.ReplayDataWriter;
import com.deyo.hypixel.type.game.replay.recordable.Recordable;
import com.deyo.hypixel.type.game.replay.recordable.RecordableType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReplayDataTest {

    @Test
    void recordablesBetweenOnlyIteratesTicksWithData() throws Exception {
        ReplayData data = new ReplayData();
        TreeMap<Integer, List<Recordable>> byTick = byTick(data);
        byTick.put(0, List.of(new TestRecordable()));
        byTick.put(10, List.of(new TestRecordable(), new TestRecordable()));
        byTick.put(1000, List.of(new TestRecordable()));

        assertEquals(3, data.getRecordablesBetween(0, 10).size());
        assertEquals(0, data.getRecordablesBetween(11, 999).size());

        List<Recordable> seen = new ArrayList<>();
        data.forEachRecordableBetween(0, 1000, seen::add);
        assertEquals(4, seen.size());
    }

    @Test
    void maxTickUsesTheLastRecordedTick() throws Exception {
        ReplayData data = new ReplayData();
        assertEquals(0, data.getMaxTick());

        TreeMap<Integer, List<Recordable>> byTick = byTick(data);
        byTick.put(12, List.of(new TestRecordable()));
        byTick.put(7, List.of(new TestRecordable()));

        assertEquals(12, data.getMaxTick());
    }

    @SuppressWarnings("unchecked")
    private static TreeMap<Integer, List<Recordable>> byTick(ReplayData data) throws Exception {
        Field field = ReplayData.class.getDeclaredField("recordablesByTick");
        field.setAccessible(true);
        return (TreeMap<Integer, List<Recordable>>) field.get(data);
    }

    private static final class TestRecordable implements Recordable {
        private int tick;

        @Override
        public RecordableType getType() {
            return RecordableType.PLAYER_ARM_SWING;
        }

        @Override
        public void write(ReplayDataWriter writer) {
        }

        @Override
        public void read(ReplayDataReader reader) {
        }

        @Override
        public int getTick() {
            return tick;
        }

        @Override
        public void setTick(int tick) {
            this.tick = tick;
        }
    }
}
