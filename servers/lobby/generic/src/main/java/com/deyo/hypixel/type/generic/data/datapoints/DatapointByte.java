package com.deyo.hypixel.type.generic.data.datapoints;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.type.generic.data.Datapoint;

public class DatapointByte extends Datapoint<Byte> {
    private static final JacksonSerializer<Byte> serializer = new JacksonSerializer<>(Byte.class);

    public DatapointByte(String key, Byte value) {
        super(key, value, serializer);
    }

    public DatapointByte(String key) {
        super(key, null, serializer);
    }
}
