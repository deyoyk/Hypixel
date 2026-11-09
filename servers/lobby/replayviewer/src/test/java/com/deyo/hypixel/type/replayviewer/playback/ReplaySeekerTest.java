package com.deyo.hypixel.type.replayviewer.playback;

import com.deyo.hypixel.type.game.replay.recordable.RecordablePlayerChat;
import com.deyo.hypixel.type.game.replay.recordable.RecordablePlayerDeath;
import com.deyo.hypixel.type.game.replay.recordable.RecordablePlayerRespawn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReplaySeekerTest {

    @Test
    void deathAndRespawnArePersistentAcrossSeeks() {
        assertTrue(ReplaySeeker.isPersistentRecordable(new RecordablePlayerDeath()));
        assertTrue(ReplaySeeker.isPersistentRecordable(new RecordablePlayerRespawn()));
    }

    @Test
    void chatRecordablesAreNotReplayedDuringSeeks() {
        assertFalse(ReplaySeeker.isPersistentRecordable(new RecordablePlayerChat(1, "hello", false)));
    }
}
