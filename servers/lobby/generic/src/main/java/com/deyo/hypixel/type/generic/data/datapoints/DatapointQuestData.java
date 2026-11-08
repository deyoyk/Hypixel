package com.deyo.hypixel.type.generic.data.datapoints;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.type.generic.data.Datapoint;
import com.deyo.hypixel.type.generic.quest.QuestData;

public class DatapointQuestData extends Datapoint<QuestData> {
    private static final JacksonSerializer<QuestData> serializer = new JacksonSerializer<>(QuestData.class);

    public DatapointQuestData(String key, QuestData value) {
        super(key, value != null ? value : new QuestData(), serializer);
    }

    public DatapointQuestData(String key) {
        super(key, new QuestData(), serializer);
    }
}
