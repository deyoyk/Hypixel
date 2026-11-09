package com.deyo.hypixel.type.replayviewer;

import com.deyo.hypixel.type.game.replay.ReplayMetadata;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReplayTablistModuleTest {

    @Test
    void playerEntriesUseTheirTeamColour() {
        UUID redPlayer = UUID.randomUUID();
        UUID lonePlayer = UUID.randomUUID();
        ReplayMetadata metadata = ReplayMetadata.builder()
                .players(Map.of(redPlayer, "RedGuy", lonePlayer, "SoloGuy"))
                .teams(Map.of("RED", List.of(redPlayer)))
                .teamInfo(Map.of("RED", new ReplayMetadata.TeamInfo("Red", "<c>", 0xFF0000)))
                .build();

        var entries = ReplayTablistModule.entriesFor(metadata);
        List<String> rendered = entries.stream().map(entry -> entry.content().serialize()).toList();

        assertEquals(2, rendered.size());
        assertTrue(rendered.stream().anyMatch(value -> value.startsWith("<c>") && value.contains("RedGuy")),
                rendered.toString());
        assertTrue(rendered.stream().anyMatch(value -> value.startsWith("<7>") && value.contains("SoloGuy")),
                rendered.toString());
    }
}
