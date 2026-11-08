package com.deyo.hypixel.type.generic.data.datapoints;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.type.generic.achievement.AchievementData;
import com.deyo.hypixel.type.generic.data.Datapoint;

public class DatapointAchievementData extends Datapoint<AchievementData> {
    private static final JacksonSerializer<AchievementData> serializer = new JacksonSerializer<>(AchievementData.class);

    public DatapointAchievementData(String key, AchievementData value) {
        super(key, value != null ? value : new AchievementData(), serializer);
    }

    public DatapointAchievementData(String key) {
        super(key, new AchievementData(), serializer);
    }
}
