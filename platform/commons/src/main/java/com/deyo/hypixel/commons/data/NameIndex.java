package com.deyo.hypixel.commons.data;

import redis.clients.jedis.Jedis;

import java.util.UUID;

public final class NameIndex {
    private static final String KEY = "hsb:name2uuid";

    private NameIndex() {}

    public static void index(String name, UUID uuid) {
        if (DeyoData.jedisPool() == null || name == null) return;
        try (Jedis jedis = DeyoData.jedisPool().getResource()) {
            jedis.hset(KEY, name.toLowerCase(), uuid.toString());
        }
    }

    public static UUID lookup(String name) {
        if (DeyoData.jedisPool() == null || name == null) return null;
        try (Jedis jedis = DeyoData.jedisPool().getResource()) {
            String value = jedis.hget(KEY, name.toLowerCase());
            return value == null ? null : UUID.fromString(value);
        }
    }
}
