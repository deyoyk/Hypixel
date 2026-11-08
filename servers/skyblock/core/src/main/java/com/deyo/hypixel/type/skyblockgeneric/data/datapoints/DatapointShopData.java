package com.deyo.hypixel.type.skyblockgeneric.data.datapoints;

import com.deyo.hypixel.commons.skyblock.PlayerShopData;
import com.deyo.hypixel.commons.protocol.serializers.PlayerShopDataSerializer;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDatapoint;

public class DatapointShopData extends SkyBlockDatapoint<PlayerShopData> {
    private static final PlayerShopDataSerializer serializer = new PlayerShopDataSerializer();

    public DatapointShopData(String key, PlayerShopData value) {
        super(key, value, serializer);
    }

    public DatapointShopData(String key) {
        super(key, null, serializer);
    }
}
