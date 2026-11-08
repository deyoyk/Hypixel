package com.deyo.hypixel.type.generic.data.datapoints;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.type.generic.data.Datapoint;

public class DatapointInteger extends Datapoint<Integer> {
    private static final JacksonSerializer<Integer> serializer = new JacksonSerializer<>(Integer.class);

    public DatapointInteger(String key, Integer value) {
        super(key, value, serializer);
    }

    public DatapointInteger(String key) {
        super(key, null, serializer);
    }
}
