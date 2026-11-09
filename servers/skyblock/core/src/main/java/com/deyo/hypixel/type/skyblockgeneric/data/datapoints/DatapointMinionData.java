package com.deyo.hypixel.type.skyblockgeneric.data.datapoints;

import com.deyo.hypixel.commons.protocol.Serializer;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDatapoint;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class DatapointMinionData extends SkyBlockDatapoint<DatapointMinionData.ProfileMinionData> {

    public DatapointMinionData(String key, DatapointMinionData.ProfileMinionData value) {
        super(key, value, new Serializer<>() {
            @Override
            public String serialize(DatapointMinionData.ProfileMinionData value) {
                StringBuilder sb = new StringBuilder();
                sb.append("{");

                for (Map.Entry<String, List<Integer>> entry : value.craftedMinions) {
                    sb.append(entry.getKey()).append(":[");
                    boolean first = true;
                    for (Integer tier : entry.getValue()) {
                        if (!first) sb.append(',');
                        sb.append(tier);
                        first = false;
                    }
                    sb.append("],");
                }

                if (sb.length() > 1) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                sb.append("}");
                return sb.toString();
            }

            @Override
            public DatapointMinionData.ProfileMinionData deserialize(String json) {
                JSONObject jsonObject = new JSONObject(json);
                List<Map.Entry<String, List<Integer>>> craftedMinions = new ArrayList<>();
                for (String key : jsonObject.keySet()) {
                    List<Integer> list = new ArrayList<>();
                    for (Object object : jsonObject.getJSONArray(key)) {
                        list.add((int) object);
                    }
                    craftedMinions.add(Map.entry(key, list));
                }
                return new ProfileMinionData(craftedMinions);
            }

            @Override
            public ProfileMinionData clone(ProfileMinionData value) {
                List<Map.Entry<String, List<Integer>>> craftedMinions = new ArrayList<>();
                for (Map.Entry<String, List<Integer>> entry : value.craftedMinions) {
                    craftedMinions.add(Map.entry(entry.getKey(), new ArrayList<>(entry.getValue())));
                }
                return new ProfileMinionData(craftedMinions);
            }
        });
    }

    public DatapointMinionData(String key) {
        this(key, new ProfileMinionData(new ArrayList<>()));
    }

    public record ProfileMinionData(List<Map.Entry<String, List<Integer>>> craftedMinions) {
        private static final NavigableMap<Integer, Integer> SLOTS_FOR_CRAFTED = new TreeMap<>(Map.of(
                0, 5,
                5, 6,
                11, 7
        ));

        public int getUniqueMinions() {
            return (int) craftedMinions.stream().map(Map.Entry::getKey).distinct().count();
        }

        public int getSlots() {
            Map.Entry<Integer, Integer> entry = SLOTS_FOR_CRAFTED.floorEntry(getUniqueMinions());
            return entry == null ? 0 : entry.getValue();
        }
    }
}
