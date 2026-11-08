package com.deyo.hypixel.type.generic.data.datapoints;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.type.generic.data.Datapoint;

public class DatapointBoolean extends Datapoint<Boolean> {
    private static final JacksonSerializer<Boolean> serializer = new JacksonSerializer<>(Boolean.class);

    public DatapointBoolean(String key, Boolean value) {
        super(key, value, serializer);
    }

    public DatapointBoolean(String key) {
        super(key, null, serializer);
    }
}