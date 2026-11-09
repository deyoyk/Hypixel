package com.deyo.hypixel.type.generic.leaderboard;

import com.deyo.hypixel.commons.leaderboard.LeaderboardPlayerFilter;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LeaderboardPlayerFilterResolverTest {

    @Test
    void allFilterAllowsEveryPlayerWithoutResolvingSocialData() {
        LeaderboardPlayerFilterResolver.AllowedPlayers allowed =
                LeaderboardPlayerFilterResolver.resolve(null, LeaderboardPlayerFilter.ALL);

        assertTrue(allowed.unrestricted());
        assertTrue(allowed.allows(UUID.randomUUID()));
    }

    @Test
    void restrictedFilterOnlyAllowsSelectedPlayers() {
        UUID included = UUID.randomUUID();
        UUID excluded = UUID.randomUUID();
        LeaderboardPlayerFilterResolver.AllowedPlayers allowed =
                LeaderboardPlayerFilterResolver.AllowedPlayers.of(Set.of(included));

        assertFalse(allowed.unrestricted());
        assertTrue(allowed.allows(included));
        assertFalse(allowed.allows(excluded));
        assertFalse(LeaderboardPlayerFilterResolver.AllowedPlayers.none().allows(included));
    }

    @Test
    void limitEntriesFiltersAndPreservesLeaderboardOrder() {
        UUID first = UUID.randomUUID();
        UUID second = UUID.randomUUID();
        UUID third = UUID.randomUUID();
        List<LeaderboardService.LeaderboardEntry> entries = List.of(
                new LeaderboardService.LeaderboardEntry(first, 1, 30),
                new LeaderboardService.LeaderboardEntry(second, 2, 20),
                new LeaderboardService.LeaderboardEntry(third, 3, 10)
        );

        List<LeaderboardService.LeaderboardEntry> limited =
                LeaderboardPlayerFilterResolver.limitEntries(
                        entries, LeaderboardPlayerFilterResolver.AllowedPlayers.of(Set.of(first, third)), 1);

        assertEquals(List.of(entries.get(0)), limited);
    }

    @Test
    void limitEntriesReturnsAllEntriesForUnrestrictedFilter() {
        List<LeaderboardService.LeaderboardEntry> entries = List.of(
                new LeaderboardService.LeaderboardEntry(UUID.randomUUID(), 1, 30),
                new LeaderboardService.LeaderboardEntry(UUID.randomUUID(), 2, 20)
        );

        assertEquals(entries, LeaderboardPlayerFilterResolver.limitEntries(
                entries, LeaderboardPlayerFilterResolver.AllowedPlayers.all(), 10));
    }
}
