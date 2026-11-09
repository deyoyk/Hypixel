package com.deyo.hypixel.commons.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import org.tinylog.Logger;

public final class ProxyHeartbeat {
    private static final String KEY = "proxy:heartbeat";
    private static final int TTL_SECONDS = 6;

    private static volatile JedisPool pool;

    private ProxyHeartbeat() {}

    public static synchronized void init(String redisUri) {
        if (pool == null) {
            pool = RedisConnectionPool.connect(redisUri, RedisConnectionPool.Settings.standard());
        }
    }

    public static void beat() {
        try (Jedis jedis = pool.getResource()) {
            jedis.setex(KEY, TTL_SECONDS, Long.toString(System.currentTimeMillis()));
        } catch (RuntimeException exception) {
            Logger.warn(exception, "Failed to publish proxy heartbeat");
        }
    }

    public static boolean isProxyAlive() {
        JedisPool current = pool;
        if (current == null) return false;
        try (Jedis jedis = current.getResource()) {
            return jedis.exists(KEY);
        }
    }

    public static synchronized void shutdown() {
        if (pool != null) {
            pool.close();
            pool = null;
        }
    }
}
