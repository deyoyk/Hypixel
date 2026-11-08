package com.deyo.hypixel.type.generic.data.datapoints;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.type.generic.data.Datapoint;
import com.deyo.hypixel.type.generic.user.categories.Rank;

public class DatapointRank extends Datapoint<Rank> {
    private static final JacksonSerializer<Rank> serializer = new JacksonSerializer<>(Rank.class);

    public DatapointRank(String key, Rank value) {
        super(key, value, serializer);
    }

    public DatapointRank(String key) {
        super(key, null, serializer);
    }
}