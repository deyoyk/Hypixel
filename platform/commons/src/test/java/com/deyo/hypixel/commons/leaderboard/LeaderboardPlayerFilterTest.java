package com.deyo.hypixel.commons.leaderboard;

import com.deyo.hypixel.commons.bedwars.LeaderboardPreferences;
import com.deyo.hypixel.commons.murdermystery.MurderMysteryLeaderboardPreferences;
import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.skywars.SkywarsLeaderboardPreferences;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LeaderboardPlayerFilterTest {

    @Test
    void cyclesThroughEveryFilter() {
        LeaderboardPlayerFilter filter = LeaderboardPlayerFilter.ALL;
        for (int i = 1; i < LeaderboardPlayerFilter.values().length; i++) {
            filter = filter.next();
            assertNotNull(filter);
        }
        LeaderboardPlayerFilter wrapped = filter.next();
        assertEquals(LeaderboardPlayerFilter.ALL, wrapped);
        assertEquals(LeaderboardPlayerFilter.GUILD_MEMBERS, wrapped.previous());
    }

    @Test
    void skywarsPreferencesDefaultMissingFilterToAll() {
        JacksonSerializer<SkywarsLeaderboardPreferences> serializer =
                new JacksonSerializer<>(SkywarsLeaderboardPreferences.class);
        String legacy = """
                {"period":"WEEKLY","mode":"ALL","view":"TOP_10","textAlignment":"CENTER"}
                """;

        assertEquals(LeaderboardPlayerFilter.ALL, serializer.deserialize(legacy).playerFilter());
        assertEquals(LeaderboardPlayerFilter.ALL, SkywarsLeaderboardPreferences.defaults().playerFilter());
    }

    @Test
    void murderMysteryPreferencesDefaultMissingFilterToAll() {
        JacksonSerializer<MurderMysteryLeaderboardPreferences> serializer =
                new JacksonSerializer<>(MurderMysteryLeaderboardPreferences.class);
        String legacy = """
                {"period":"WEEKLY","mode":"CLASSIC","view":"TOP_10","textAlignment":"CENTER"}
                """;

        assertEquals(LeaderboardPlayerFilter.ALL, serializer.deserialize(legacy).playerFilter());
        assertEquals(LeaderboardPlayerFilter.ALL, MurderMysteryLeaderboardPreferences.defaults().playerFilter());
    }

    @Test
    void bedwarsPreferencesDefaultMissingFilterToAll() {
        JacksonSerializer<LeaderboardPreferences> serializer =
                new JacksonSerializer<>(LeaderboardPreferences.class);
        String legacy = """
                {"period":"LIFETIME","mode":"ALL","view":"TOP_10","textAlignment":"CENTER"}
                """;

        assertEquals(LeaderboardPlayerFilter.ALL, serializer.deserialize(legacy).playerFilter());
        assertEquals(LeaderboardPlayerFilter.ALL, LeaderboardPreferences.defaults().playerFilter());
    }

    @Test
    void preferencesRoundTripSelectedFilter() {
        SkywarsLeaderboardPreferences preferences = new SkywarsLeaderboardPreferences(
                SkywarsLeaderboardPreferences.defaults().period(),
                SkywarsLeaderboardPreferences.defaults().mode(),
                SkywarsLeaderboardPreferences.defaults().view(),
                SkywarsLeaderboardPreferences.defaults().textAlignment(),
                LeaderboardPlayerFilter.BEST_FRIENDS);
        JacksonSerializer<SkywarsLeaderboardPreferences> serializer =
                new JacksonSerializer<>(SkywarsLeaderboardPreferences.class);

        assertEquals(preferences, serializer.deserialize(serializer.serialize(preferences)));
    }
}
