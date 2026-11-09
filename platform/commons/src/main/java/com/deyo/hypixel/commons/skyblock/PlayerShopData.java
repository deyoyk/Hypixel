package com.deyo.hypixel.commons.skyblock;

import com.deyo.hypixel.commons.Tuple;
import com.deyo.hypixel.commons.skyblock.item.UnderstandableSkyBlockItem;
import com.deyo.hypixel.commons.protocol.serializers.UnderstandableSkyBlockItemSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerShopData {
    public static final int MAXIMUM_STOCK = 640;
    private static final UnderstandableSkyBlockItemSerializer ITEM_SERIALIZER =
            new UnderstandableSkyBlockItemSerializer();

    private final Map<String, Tuple<Integer, Long>> itemData;
    private final List<Tuple<UnderstandableSkyBlockItem, Integer>> buybackData;

    public PlayerShopData() {
        this.itemData = new HashMap<>();
        this.buybackData = new ArrayList<>();
    }

    public boolean canPurchase(UnderstandableSkyBlockItem item, int amount) {
        updateStock(item);
        return getStock(item) >= amount;
    }

    public long getLastPurchase(UnderstandableSkyBlockItem item) {
        if (!itemData.containsKey(item.itemKey().name()))
            return 0;
        return itemData.get(item.itemKey().name()).getValue();
    }

    public int getStock(UnderstandableSkyBlockItem item) {
        if (!itemData.containsKey(item.itemKey().name()))
            return MAXIMUM_STOCK;
        updateStock(item);

        return MAXIMUM_STOCK - itemData.get(item.itemKey().name()).getKey();
    }

    private boolean updateStock(UnderstandableSkyBlockItem item) {
        if ((System.currentTimeMillis() - getLastPurchase(item)) > 1200000) {
            resetStock(item);
            return true;
        }
        return false;
    }

    public void documentPurchase(UnderstandableSkyBlockItem item, int amount) {
        Tuple<Integer, Long> data;
        if (itemData.containsKey(item.itemKey().name())) {
            data = itemData.get(item.itemKey().name());
            data.setKey(data.getKey() + amount);
            data.setValue(System.currentTimeMillis());
        } else {
            data = new Tuple<>(amount, System.currentTimeMillis());
        }
        itemData.put(item.itemKey().name(), data);
    }

    private void resetStock(UnderstandableSkyBlockItem item) {
        itemData.put(item.itemKey().name(), new Tuple<>(0, System.currentTimeMillis()));
    }

    public void resetStocks() {
        itemData.keySet().forEach(it -> itemData.put(it, new Tuple<>(0, System.currentTimeMillis())));
    }

    public int pushBuyback(UnderstandableSkyBlockItem item, int amount) {
        buybackData.add(new Tuple<>(item, amount));
        return buybackData.size() - 1;
    }

    public Tuple<UnderstandableSkyBlockItem, Integer> popBuyback() {
        if (buybackData.isEmpty())
            throw new IndexOutOfBoundsException("Woah there!");
        Tuple<UnderstandableSkyBlockItem, Integer> last = buybackData.getLast();
        buybackData.removeLast();
        return last;
    }

    public Tuple<UnderstandableSkyBlockItem, Integer> lastBuyback() {
        if (buybackData.isEmpty())
            throw new IndexOutOfBoundsException("Woah there!");
        return buybackData.getLast();
    }

    public Tuple<UnderstandableSkyBlockItem, Integer> firstBuyback() {
        if (buybackData.isEmpty())
            throw new IndexOutOfBoundsException("Woah there!");
        return buybackData.getFirst();
    }

    public boolean hasAnythingToBuyback() {
        return !buybackData.isEmpty();
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> itemDataM = new HashMap<>();
        List<Map<String, Object>> buybacks = new ArrayList<>();
        itemData.forEach((item, stockNTime) -> {
            itemDataM.put(item, stockNTime.getKey() + "/" + stockNTime.getValue());
        });
        buybackData.forEach((tuple) -> buybacks.add(Map.of(
                "item", ITEM_SERIALIZER.serialize(tuple.getKey()),
                "amount", tuple.getValue()
        )));
        map.put("purchases", itemDataM);
        map.put("buyback", buybacks);
        return map;
    }

    public void deserialize(Map<String, Object> map) {
        ((Map<String, Object>) map.get("purchases")).forEach((itemID, stockNTimeO) -> {
            String stockNTime = stockNTimeO.toString();
            Integer stockBought = Integer.parseInt(stockNTime.split("/")[0]);
            Long lastPurchase = Long.parseLong(stockNTime.split("/")[1]);

            itemData.put(itemID, new Tuple<>(stockBought, lastPurchase));
        });

        Object buybackObject = map.get("buyback");
        if (buybackObject instanceof List<?> entries) {
            for (Object rawEntry : entries) {
                if (!(rawEntry instanceof Map<?, ?> entry)) continue;
                Object itemJson = entry.get("item");
                Object amount = entry.get("amount");
                if (itemJson == null || amount == null) continue;
                buybackData.add(new Tuple<>(
                        ITEM_SERIALIZER.deserialize(itemJson.toString()),
                        Integer.parseInt(amount.toString())));
            }
        } else if (buybackObject instanceof Map<?, ?> entries) {
            entries.forEach((itemJson, amount) -> buybackData.add(new Tuple<>(
                    ITEM_SERIALIZER.deserialize(itemJson.toString()),
                    Integer.parseInt(amount.toString()))));
        }
    }
}
