package com.deyo.hypixel.commons.skyblock.auctions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import com.deyo.hypixel.commons.skyblock.item.UnderstandableSkyBlockItem;
import com.deyo.hypixel.commons.protocol.serializers.AuctionItemSerializer;
import com.deyo.hypixel.commons.protocol.serializers.UnderstandableSkyBlockItemSerializer;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AuctionItem {
    private static final AuctionItemSerializer<AuctionItem> SERIALIZER = new AuctionItemSerializer<>();
    private static final UnderstandableSkyBlockItemSerializer ITEM_SERIALIZER = new UnderstandableSkyBlockItemSerializer();
    private UUID uuid;
    private UUID originator;
    private UnderstandableSkyBlockItem item;
    private long endTime;

    private boolean isBin;
    private Integer startingPrice;

    private List<Bid> bids;

    public AuctionItem() {
        this.bids = new ArrayList<>();
    }

    public AuctionItem(UnderstandableSkyBlockItem item, UUID originator, long endTime, boolean isBin, Long startingPrice) {
        this.uuid = UUID.randomUUID();
        this.originator = originator;
        this.item = item;
        this.endTime = endTime;
        this.isBin = isBin;
        this.startingPrice = Math.toIntExact(startingPrice);

        this.bids = new ArrayList<>();
    }

    @JsonValue
    public String toSerializedString() {
        return SERIALIZER.serialize(this);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AuctionItem fromSerializedString(String json) {
        return SERIALIZER.deserialize(json);
    }

    public Document toDocument() {
        List<String> serializedBids = bids == null
                ? List.of()
                : bids.stream().map(Bid::toString).toList();
        return new Document()
                .append("_id", uuid.toString())
                .append("originator", originator.toString())
                .append("item", ITEM_SERIALIZER.serialize(item))
                .append("end", endTime)
                .append("bin", isBin)
                .append("starting-price", startingPrice)
                .append("bids", serializedBids);
    }

    public static AuctionItem fromDocument(Document document) {
        AuctionItem item = new AuctionItem();
        item.setUuid(UUID.fromString(document.getString("_id")));
        item.setOriginator(UUID.fromString(document.getString("originator")));
        item.setItem(ITEM_SERIALIZER.deserialize(document.getString("item")));
        Long end = document.getLong("end");
        item.setEndTime(end != null ? end : 0L);
        item.setBin(Boolean.TRUE.equals(document.getBoolean("bin")));
        item.setStartingPrice(document.getInteger("starting-price"));
        List<String> bids = document.getList("bids", String.class);
        item.setBids(bids == null ? List.of() : bids.stream().map(Bid::fromString).toList());
        return item;
    }

    public record Bid(Long timestamp, UUID uuid, Long value) {
        public @NotNull String toString() {
            return timestamp + "," + uuid + "," + value;
        }

        public static Bid fromString(String string) {
            String[] parts = string.split(",");
            return new Bid(Long.parseLong(parts[0]), UUID.fromString(parts[1]), Long.parseLong(parts[2]));
        }
    }
}
