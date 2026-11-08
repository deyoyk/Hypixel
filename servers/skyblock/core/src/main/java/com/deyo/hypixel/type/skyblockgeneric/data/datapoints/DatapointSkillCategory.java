package com.deyo.hypixel.type.skyblockgeneric.data.datapoints;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDatapoint;
import com.deyo.hypixel.type.skyblockgeneric.skill.SkillCategories;

public class DatapointSkillCategory extends SkyBlockDatapoint<SkillCategories> {
    private static final JacksonSerializer<SkillCategories> serializer = new JacksonSerializer<>(SkillCategories.class);

    public DatapointSkillCategory(String key, SkillCategories value) {
        super(key, value, serializer);
    }

    public DatapointSkillCategory(String key) {
        super(key, null, serializer);
    }
}