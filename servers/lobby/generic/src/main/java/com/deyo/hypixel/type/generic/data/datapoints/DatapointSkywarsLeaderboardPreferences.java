package com.deyo.hypixel.type.generic.data.datapoints;

import com.deyo.hypixel.commons.skywars.SkywarsLeaderboardPreferences;
import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.type.generic.data.Datapoint;

public class DatapointSkywarsLeaderboardPreferences extends Datapoint<SkywarsLeaderboardPreferences> {
    private static final JacksonSerializer<SkywarsLeaderboardPreferences> serializer =
            new JacksonSerializer<>(SkywarsLeaderboardPreferences.class);

    public DatapointSkywarsLeaderboardPreferences(String key, SkywarsLeaderboardPreferences value) {
        super(key, value, serializer);
    }

    public DatapointSkywarsLeaderboardPreferences(String key) {
        this(key, SkywarsLeaderboardPreferences.defaults());
    }
}
