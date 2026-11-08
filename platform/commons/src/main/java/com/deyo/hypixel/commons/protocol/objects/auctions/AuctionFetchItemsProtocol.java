package com.deyo.hypixel.commons.protocol.objects.auctions;

import com.deyo.hypixel.commons.protocol.JacksonSerializer;
import com.deyo.hypixel.commons.skyblock.auctions.AuctionCategories;
import com.deyo.hypixel.commons.skyblock.auctions.AuctionsFilter;
import com.deyo.hypixel.commons.skyblock.auctions.AuctionsSorting;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.Serializer;
import com.deyo.hypixel.commons.skyblock.auctions.AuctionItem;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public class AuctionFetchItemsProtocol extends RedisProtocol<
        AuctionFetchItemsProtocol.AuctionFetchItemsMessage,
        AuctionFetchItemsProtocol.AuctionFetchItemsResponse> {
    private static final Serializer<AuctionFetchItemsMessage> SERIALIZER =
            new JacksonSerializer<>(AuctionFetchItemsMessage.class);
    private static final Serializer<AuctionFetchItemsResponse> RETURN_SERIALIZER =
            new JacksonSerializer<>(AuctionFetchItemsResponse.class);

    @Override
    public Serializer<AuctionFetchItemsMessage> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Serializer<AuctionFetchItemsResponse> getReturnSerializer() {
        return RETURN_SERIALIZER;
    }

    public record AuctionFetchItemsMessage(
            AuctionsSorting sorting,
            AuctionsFilter filter,
            AuctionCategories category
    ) {}

    public record AuctionFetchItemsResponse(
            List<AuctionItem> items,
            boolean success,
            @Nullable String error
    ) {}
}
