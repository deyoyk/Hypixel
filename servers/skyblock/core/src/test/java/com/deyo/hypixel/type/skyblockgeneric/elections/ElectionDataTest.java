package com.deyo.hypixel.type.skyblockgeneric.elections;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ElectionDataTest {

    @Test
    void newElectionCandidatesExposeTheirFullPerkPool() {
        ElectionData data = new ElectionData();
        data.startNewElection(10);

        assertEquals(5, data.getCandidates().size());
        for (ElectionData.CandidateData candidate : data.getCandidates()) {
            SkyBlockMayor mayor = SkyBlockMayor.valueOf(candidate.getMayorName());
            List<String> expectedPool = Arrays.stream(mayor.getAllPerks()).map(Enum::name).toList();

            assertEquals(expectedPool, candidate.getPerkPool());
            assertFalse(candidate.getActivePerks().isEmpty());
            assertTrue(expectedPool.containsAll(candidate.getActivePerks()));
        }
    }

    @Test
    void specialElectionCandidatesHaveEveryPerkActive() {
        ElectionData data = new ElectionData();
        data.startNewElection(8);

        assertEquals(5, data.getCandidates().size());
        ElectionData.CandidateData candidate = data.getCandidates().getLast();
        SkyBlockMayor mayor = SkyBlockMayor.valueOf(candidate.getMayorName());

        assertTrue(mayor.isSpecial());
        assertEquals(Arrays.stream(mayor.getAllPerks()).map(Enum::name).toList(), candidate.getPerkPool());
        assertEquals(candidate.getPerkPool(), candidate.getActivePerks());
    }

    @Test
    void perkPoolsSurviveSerialization() {
        ElectionData data = new ElectionData();
        data.startNewElection(10);

        ElectionData parsed = new com.google.gson.Gson().fromJson(
                new com.google.gson.Gson().toJson(data), ElectionData.class);

        assertEquals(data.getCandidates().size(), parsed.getCandidates().size());
        for (int index = 0; index < data.getCandidates().size(); index++) {
            assertEquals(
                    data.getCandidates().get(index).getPerkPool(),
                    parsed.getCandidates().get(index).getPerkPool());
        }
    }
}
