package com.deyo.hypixel.commons.guild;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GuildDataTest {

    @Test
    void legacyDocumentsWithoutListsStillLoad() {
        GuildData data = GuildData.getStaticSerializer().deserialize("{\"name\":\"Legacy\"}");

        assertNotNull(data.getMembers());
        assertNotNull(data.getRanks());
        assertEquals(3, data.getRanks().size());
    }

    @Test
    void guildGamesDefaultEmptyAndSurviveSerialization() {
        GuildData data = new GuildData(UUID.randomUUID(), "Test", UUID.randomUUID());
        assertTrue(data.getGames().isEmpty());

        data.setGames(List.of("BED_WARS", "SKYWARS"));
        GuildData loaded = GuildData.getStaticSerializer().deserialize(
                GuildData.getStaticSerializer().serialize(data));

        assertEquals(List.of("BED_WARS", "SKYWARS"), loaded.getGames());
        assertTrue(GuildData.getStaticSerializer().deserialize("{\"name\":\"Legacy\"}").getGames().isEmpty());
    }

    @Test
    void rankNavigationMovesToAdjacentPriorities() {
        GuildData data = new GuildData(UUID.randomUUID(), "Test", UUID.randomUUID());
        GuildRank officer = data.getRank("Officer");

        assertEquals(GuildRank.guildMaster().getName(), data.getNextRankUp(officer).getName());
        assertEquals(GuildRank.member().getName(), data.getNextRankDown(officer).getName());
        assertNull(data.getNextRankUp(GuildRank.guildMaster()));
        assertNull(data.getNextRankDown(GuildRank.member()));
        assertNull(data.getNextRankUp(null));
        assertNull(data.getNextRankDown(null));
    }

    @Test
    void gexpThresholdsRaiseTheGuildLevel() {
        GuildData data = new GuildData(UUID.randomUUID(), "Test", UUID.randomUUID());

        data.addGexp(99_999);
        assertEquals(0, data.getLevel());

        data.addGexp(1);
        assertEquals(1, data.getLevel());
    }

    @Test
    void auditLogIsCappedAtFiveHundredEntries() {
        GuildData data = new GuildData(UUID.randomUUID(), "Test", UUID.randomUUID());

        for (int index = 0; index < 510; index++) {
            data.addAuditLog("entry-" + index);
        }

        assertEquals(500, data.getAuditLog().size());
        assertEquals("entry-509", data.getAuditLog().getFirst());
    }

    @Test
    void dailyProgressResetsWhenTheDateChanges() {
        GuildData data = new GuildData(UUID.randomUUID(), "Test", UUID.randomUUID());
        data.setDailyGexp(500);
        data.setDailyWins(3);
        data.setDailyProgressDate("2000-01-01");

        data.resetDailyProgressIfNeeded();

        assertEquals(0, data.getDailyGexp());
        assertEquals(0, data.getDailyWins());
        assertEquals(LocalDate.now().toString(), data.getDailyProgressDate());
    }
}
