package com.deyo.hypixel.type.skyblockgeneric.user;

import com.deyo.hypixel.commons.text.TextBody;
import com.deyo.hypixel.type.skyblockgeneric.elections.ElectionData;
import com.deyo.hypixel.type.skyblockgeneric.region.RegionType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegionScoreboardManagerTest {

    @Test
    void electionRoomAddsItsSectionAndHidesDefaults() {
        RegionScoreboardManager.RegionScoreboard scoreboard =
                RegionScoreboardManager.get(RegionType.ELECTION_ROOM);
        assertNotNull(scoreboard);
        assertTrue(scoreboard.hidesDefaultSections());

        ElectionData.CandidateData candidate = new ElectionData.CandidateData();
        candidate.setIndex(0);
        candidate.setMayorName("BARRY");

        TextBody body = new TextBody();
        scoreboard.renderer().accept(new RegionScoreboardManager.Context(
                body, 5, List.of(candidate), Map.of("BARRY", 10L), 10L));

        assertTrue(body.has("election"));
    }

    @Test
    void otherRegionsUseTheDefaultSections() {
        assertNull(RegionScoreboardManager.get(RegionType.TAVERN));
        assertNull(RegionScoreboardManager.get(null));
    }
}
