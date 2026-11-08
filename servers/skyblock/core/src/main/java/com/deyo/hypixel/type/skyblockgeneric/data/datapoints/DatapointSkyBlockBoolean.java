package com.deyo.hypixel.type.skyblockgeneric.data.datapoints;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDatapoint;

public class DatapointSkyBlockBoolean extends SkyBlockDatapoint<Boolean> {
    private static final JacksonSerializer<Boolean> serializer = new JacksonSerializer<>(Boolean.class);

    public DatapointSkyBlockBoolean(String key, Boolean value) {
        super(key, value, serializer);
    }

    public DatapointSkyBlockBoolean(String key) {
        super(key, null, serializer);
    }
}