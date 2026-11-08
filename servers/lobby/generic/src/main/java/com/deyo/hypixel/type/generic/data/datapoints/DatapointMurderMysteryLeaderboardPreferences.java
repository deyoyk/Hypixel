package com.deyo.hypixel.type.generic.data.datapoints;

import com.deyo.hypixel.commons.murdermystery.MurderMysteryLeaderboardPreferences;
import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.type.generic.data.Datapoint;

public class DatapointMurderMysteryLeaderboardPreferences extends Datapoint<MurderMysteryLeaderboardPreferences> {
    private static final JacksonSerializer<MurderMysteryLeaderboardPreferences> serializer =
            new JacksonSerializer<>(MurderMysteryLeaderboardPreferences.class);

    public DatapointMurderMysteryLeaderboardPreferences(String key, MurderMysteryLeaderboardPreferences value) {
        super(key, value, serializer);
    }

    public DatapointMurderMysteryLeaderboardPreferences(String key) {
        this(key, MurderMysteryLeaderboardPreferences.defaults());
    }
}
