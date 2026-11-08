package com.deyo.hypixel.type.ravengardgeneric.data.datapoints;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.type.ravengardgeneric.data.RavengardDatapoint;

public class DatapointRavengardString extends RavengardDatapoint<String> {
    private static final JacksonSerializer<String> serializer = new JacksonSerializer<>(String.class);

    public DatapointRavengardString(String key, String value) {
        super(key, value, serializer);
    }

    public DatapointRavengardString(String key) {
        super(key, null, serializer);
    }
}
