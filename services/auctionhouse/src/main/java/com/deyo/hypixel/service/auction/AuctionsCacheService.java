package com.deyo.hypixel.service.auction;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.deyo.hypixel.commons.skyblock.auctions.AuctionsFilter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AuctionsCacheService {
    private final Cache<String, Object> cache;

    public AuctionsCacheService() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .maximumSize(100)
                .build();
    }

    @SuppressWarnings("unchecked")
    public List<Document> getAuctions(String categoryFilter, AuctionsFilter filter) {
        String cacheKey = auctionKey(categoryFilter, filter);
        List<Document> result = (List<Document>) cache.getIfPresent(cacheKey);
        if (result != null) {
            return result;
        }

        List<Document> documents = getDocuments(categoryFilter);
        long now = System.currentTimeMillis();
        List<Document> expired = new ArrayList<>();
        for (Document document : documents) {
            Long end = document.getLong("end");
            if (end != null && end < now) {
                expired.add(document);
            }
        }
        for (Document document : expired) {
            AuctionInactiveDatabase.collection.replaceOne(
                    Filters.eq("_id", document.get("_id")),
                    document,
                    new ReplaceOptions().upsert(true));
            AuctionActiveDatabase.collection.deleteOne(Filters.eq("_id", document.get("_id")));
        }
        if (!expired.isEmpty()) {
            documents.removeAll(expired);
            cache.invalidate(categoryKey(categoryFilter));
        }

        result = List.copyOf(applyFilter(documents, filter));
        cache.put(cacheKey, result);
        return result;
    }

    public static List<Document> applyFilter(List<Document> documents, AuctionsFilter filter) {
        return switch (filter) {
            case SHOW_ALL -> documents;
            case AUCTIONS_ONLY -> documents.stream()
                    .filter(document -> Boolean.FALSE.equals(document.getBoolean("bin")))
                    .toList();
            case BIN_ONLY -> documents.stream()
                    .filter(document -> Boolean.TRUE.equals(document.getBoolean("bin")))
                    .toList();
        };
    }

    @SuppressWarnings("unchecked")
    public List<Document> getDocuments(String category) {
        String cacheKey = categoryKey(category);
        Object result = cache.getIfPresent(cacheKey);
        if (result != null) {
            return (List<Document>) result;
        }

        ArrayList<Document> documents = new ArrayList<>(
                AuctionActiveDatabase.collection.find(Filters.eq("category", category.toUpperCase())).into(new ArrayList<>()));
        cache.put(cacheKey, documents);

        return documents;
    }

    static String categoryKey(String category) {
        return "docs:" + category.toUpperCase(Locale.ROOT);
    }

    static String auctionKey(String category, AuctionsFilter filter) {
        return "auctions:" + category.toUpperCase(Locale.ROOT) + ":" + filter;
    }
}
