package com.deyo.hypixel.type.skyblockgeneric.data.datapoints;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minestom.server.item.Material;
import com.deyo.hypixel.commons.skyblock.item.ItemType;
import com.deyo.hypixel.commons.skyblock.item.UnderstandableSkyBlockItem;
import com.deyo.hypixel.commons.protocol.Serializer;
import com.deyo.hypixel.type.skyblockgeneric.data.SkyBlockDatapoint;
import com.deyo.hypixel.type.skyblockgeneric.item.SkyBlockItem;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatapointQuiver extends SkyBlockDatapoint<DatapointQuiver.PlayerQuiver> {
    public static Serializer<PlayerQuiver> serializer = new Serializer<>() {
        @Override
        public String serialize(PlayerQuiver value) {
            Map<String, String> serialized = new HashMap<>();

            for (Map.Entry<Integer, SkyBlockItem> entry : value.getQuiverMap().entrySet()) {
                serialized.put(
                        String.valueOf(entry.getKey()),
                        entry.getValue().toUnderstandable().serialize()
                );
            }

            return new JSONObject(serialized).toString();
        }

        @Override
        public PlayerQuiver deserialize(String json) {
            JSONObject obj = new JSONObject(json);
            Map<Integer, SkyBlockItem> map = new HashMap<>();

            for (String key : obj.keySet()) {
                map.put(Integer.parseInt(key),
                        new SkyBlockItem(UnderstandableSkyBlockItem.deserialize(obj.getString(key))));
            }

            return new PlayerQuiver(map);
        }

        @Override
        public PlayerQuiver clone(PlayerQuiver value) {
            Map<Integer, SkyBlockItem> copy = new HashMap<>();
            value.getQuiverMap().forEach((slot, item) ->
                    copy.put(slot, item == null ? null : item.clone()));
            return new PlayerQuiver(copy);
        }
    };

    public DatapointQuiver(String key, PlayerQuiver value) {
        super(key, value, serializer);
    }

    public DatapointQuiver(String key) {
        super(key, new PlayerQuiver(), serializer);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PlayerQuiver {
        private Map<Integer, SkyBlockItem> quiverMap = new HashMap<>();

        public @Nullable SkyBlockItem getFirstItemInQuiver() {
            int slot = getFirstItemSlot();
            return slot < 0 ? null : quiverMap.get(slot);
        }

        public int getFirstItemSlot() {
            int firstSlot = -1;
            for (Map.Entry<Integer, SkyBlockItem> entry : quiverMap.entrySet()) {
                if (!isUsableItem(entry.getValue())) continue;
                if (firstSlot < 0 || entry.getKey() < firstSlot) {
                    firstSlot = entry.getKey();
                }
            }
            return firstSlot;
        }

        public @Nullable SkyBlockItem consumeFirstItem() {
            int slot = getFirstItemSlot();
            if (slot < 0) return null;

            SkyBlockItem item = quiverMap.get(slot);
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                quiverMap.remove(slot);
            }
            return item;
        }

        public @Nullable SkyBlockItem getInSlot(int slot) {
            return quiverMap.get(slot);
        }

        private static boolean isUsableItem(@Nullable SkyBlockItem item) {
            return item != null
                    && item.getAmount() > 0
                    && !item.isNA()
                    && item.getMaterial() != Material.AIR;
        }

        public boolean isEmpty() {
            return getFirstItemInQuiver() == null;
        }

        public Integer getAmountOfArrows(ItemType arrowType) {
            int amount = 0;
            for (SkyBlockItem item : quiverMap.values()) {
                if (item == null) continue;
                if (item.getAmount() == 0) continue;
                if (item.isNA()) continue;
                if (item.getMaterial() == Material.AIR) continue;

                if (item.getAttributeHandler().getPotentialType() == arrowType) {
                    amount += item.getAmount();
                }
            }
            return amount;
        }
    }
}
