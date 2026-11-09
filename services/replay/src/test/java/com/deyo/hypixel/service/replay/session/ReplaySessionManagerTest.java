package com.deyo.hypixel.service.replay.session;

import com.deyo.hypixel.commons.ServerType;
import com.deyo.hypixel.service.replay.storage.ReplayDatabase;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReplaySessionManagerTest {

    @Test
    void acceptsBatchesThatArriveDuringTheEndGracePeriod() throws Exception {
        CapturingDatabase database = new CapturingDatabase();
        ReplaySessionManager manager = new ReplaySessionManager(database);
        UUID replayId = UUID.randomUUID();
        manager.startSession(replayId, "game", ServerType.PROTOTYPE_LOBBY, "server", "SOLO",
                "map", "hash", System.currentTimeMillis(), 0, 0, Map.of(), Map.of(), Map.of());

        manager.receiveBatch(replayId, 0, 0, 10, 1, new byte[5]);
        var resultFuture = manager.endSession(replayId, System.currentTimeMillis(), 10);
        manager.receiveBatch(replayId, 1, 11, 20, 1, new byte[7]);

        var result = resultFuture.get(5, TimeUnit.SECONDS);
        manager.shutdown();

        assertTrue(result.success());
        assertEquals(12, result.compressedBytes());
        assertEquals(2, database.chunks);
    }

    private static final class CapturingDatabase extends ReplayDatabase {
        private int chunks;

        @Override
        public void saveReplayDataChunk(UUID replayId, int chunkIndex, byte[] compressedData, int startTick, int endTick) {
            chunks++;
        }

        @Override
        public void saveReplayMetadata(Document metadata) {
        }
    }
}
