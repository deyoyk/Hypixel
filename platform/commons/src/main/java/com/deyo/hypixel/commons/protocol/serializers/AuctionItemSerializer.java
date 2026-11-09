package com.deyo.hypixel.commons.protocol.serializers;

import com.deyo.hypixel.commons.skyblock.auctions.AuctionItem;
import com.deyo.hypixel.commons.skyblock.item.UnderstandableSkyBlockItem;
import com.deyo.hypixel.commons.protocol.Serializer;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuctionItemSerializer<T> implements Serializer<AuctionItem> {
    private static final UnderstandableSkyBlockItemSerializer ITEM_SERIALIZER = new UnderstandableSkyBlockItemSerializer();

    @Override
    public String serialize(AuctionItem value) {
        UUID uuid = value.getUuid();
        UUID originator = value.getOriginator();
        UnderstandableSkyBlockItem item = value.getItem();
        long endTime = value.getEndTime();
        boolean isBin = value.isBin();
        Integer startingPrice = value.getStartingPrice();

        JSONObject obj = new JSONObject();
        obj.put("uuid", uuid.toString());
        obj.put("originator", originator.toString());
        obj.put("item", ITEM_SERIALIZER.serialize(item));
        obj.put("end", endTime);
        obj.put("bin", isBin);
        obj.put("starting-price", startingPrice);
        obj.put("bids", value.getBids().stream().map(AuctionItem.Bid::toString).toList());
        return obj.toString();
    }

    @Override
    public AuctionItem deserialize(String json) {
        JSONObject obj = new JSONObject(json);
        UUID uuid = UUID.fromString(obj.getString("uuid"));
        UUID originator = UUID.fromString(obj.getString("originator"));
        UnderstandableSkyBlockItem item = ITEM_SERIALIZER.deserialize(obj.getString("item"));
        long endTime = obj.getLong("end");
        boolean isBin = obj.getBoolean("bin");
        Integer startingPrice = obj.getInt("starting-price");
        List<AuctionItem.Bid> bids = obj.getJSONArray("bids").toList().stream()
                .map(Object::toString)
                .map(AuctionItem.Bid::fromString)
                .toList();

        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setUuid(uuid);
        auctionItem.setOriginator(originator);
        auctionItem.setItem(item);
        auctionItem.setEndTime(endTime);
        auctionItem.setBin(isBin);
        auctionItem.setStartingPrice(startingPrice);
        auctionItem.setBids(bids);
        return auctionItem;
    }

    @Override
    public AuctionItem clone(AuctionItem value) {
        AuctionItem clone = new AuctionItem();
        clone.setUuid(value.getUuid());
        clone.setOriginator(value.getOriginator());
        clone.setItem(value.getItem());
        clone.setEndTime(value.getEndTime());
        clone.setBin(value.isBin());
        clone.setStartingPrice(value.getStartingPrice());
        clone.setBids(value.getBids() == null ? new ArrayList<>() : new ArrayList<>(value.getBids()));
        return clone;
    }
}