package com.deyo.hypixel.type.generic.data.datapoints;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.type.generic.data.Datapoint;

import java.util.HashMap;
import java.util.Map;

public class DatapointMapLongInteger extends Datapoint<Map<Long, Integer>> {
    private static final JacksonSerializer<Map<Long, Integer>> serializer =
            new JacksonSerializer<>((Class) Map.class);

    public DatapointMapLongInteger(String key, Map<Long, Integer> value) {
        super(key, value, serializer);
    }

    // Required for deepClone() reflection which passes HashMap.class
    public DatapointMapLongInteger(String key, HashMap<Long, Integer> value) {
        super(key, value, serializer);
    }

    public DatapointMapLongInteger(String key) {
        super(key, new HashMap<>(), serializer);
    }
}
