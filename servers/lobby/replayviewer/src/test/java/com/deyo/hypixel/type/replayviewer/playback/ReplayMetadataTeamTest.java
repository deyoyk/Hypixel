package com.deyo.hypixel.type.replayviewer.playback;

import com.deyo.hypixel.type.game.replay.ReplayMetadata;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReplayMetadataTeamTest {

    @Test
    void resolvesTheTeamContainingThePlayer() {
        UUID redPlayer = UUID.randomUUID();
        UUID bluePlayer = UUID.randomUUID();

        ReplayMetadata metadata = ReplayMetadata.builder()
                .teams(Map.of(
                        "RED", List.of(redPlayer),
                        "BLUE", List.of(bluePlayer)))
                .build();

        assertEquals("RED", metadata.getTeamId(redPlayer));
        assertEquals("BLUE", metadata.getTeamId(bluePlayer));
        assertNull(metadata.getTeamId(UUID.randomUUID()));
    }

    @Test
    void missingTeamsResolveToNull() {
        ReplayMetadata metadata = ReplayMetadata.builder().build();

        assertNull(metadata.getTeamId(UUID.randomUUID()));
        assertNull(metadata.getTeamId(null));
    }

    @Test
    void nullTeamMembersResolveToNull() {
        Map<String, List<UUID>> teams = new java.util.HashMap<>();
        teams.put("RED", null);

        ReplayMetadata metadata = ReplayMetadata.builder().teams(teams).build();

        assertNull(metadata.getTeamId(UUID.randomUUID()));
    }
}
