package com.deyo.hypixel.type.generic.data.datapoints;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.type.generic.data.Datapoint;

public class DatapointFloat extends Datapoint<Float> {
    private static final JacksonSerializer<Float> serializer = new JacksonSerializer<>(Float.class);

    public DatapointFloat(String key, Float value) {
        super(key, value, serializer);
    }

    public DatapointFloat(String key) {
        super(key, null, serializer);
    }
}
