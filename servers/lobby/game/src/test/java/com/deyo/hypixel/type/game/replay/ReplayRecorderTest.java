package com.deyo.hypixel.type.game.replay;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.commons.replay.protocol.ReplayDataReader;
import com.deyo.hypixel.commons.replay.protocol.ReplayDataWriter;
import com.deyo.hypixel.type.game.replay.recordable.Recordable;
import com.deyo.hypixel.type.game.replay.recordable.RecordableType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReplayRecorderTest {

    @Test
    void batchThresholdUsesTheBufferedCounter() {
        List<Object> sent = new ArrayList<>();
        ReplayRecorder recorder = new ReplayRecorder("game", ServerType.PROTOTYPE_LOBBY, sent::add);
        recorder.start("test", "server", "map", "hash", Map.of(), Map.of(), Map.of());

        for (int index = 0; index < 999; index++) {
            recorder.record(new TestRecordable());
        }
        assertEquals(999, recorder.getRecordableCount());

        recorder.record(new TestRecordable());
        assertEquals(0, recorder.getRecordableCount());
        assertEquals(2, sent.size());
        assertTrue(sent.get(1).getClass().getSimpleName().contains("BatchMessage"));

        assertNull(recorder.flushBatch());
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
