package com.deyo.hypixel.type.ravengardgeneric.data.datapoints;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.type.ravengardgeneric.data.RavengardDatapoint;

public class DatapointRavengardInteger extends RavengardDatapoint<Integer> {
    private static final JacksonSerializer<Integer> serializer = new JacksonSerializer<>(Integer.class);

    public DatapointRavengardInteger(String key, Integer value) {
        super(key, value, serializer);
    }

    public DatapointRavengardInteger(String key) {
        super(key, null, serializer);
    }
}
