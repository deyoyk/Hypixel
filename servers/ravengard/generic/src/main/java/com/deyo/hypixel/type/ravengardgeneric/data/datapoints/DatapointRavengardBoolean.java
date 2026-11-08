package com.deyo.hypixel.type.ravengardgeneric.data.datapoints;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.type.ravengardgeneric.data.RavengardDatapoint;

public class DatapointRavengardBoolean extends RavengardDatapoint<Boolean> {
    private static final JacksonSerializer<Boolean> serializer = new JacksonSerializer<>(Boolean.class);

    public DatapointRavengardBoolean(String key, Boolean value) {
        super(key, value, serializer);
    }

    public DatapointRavengardBoolean(String key) {
        super(key, null, serializer);
    }
}
