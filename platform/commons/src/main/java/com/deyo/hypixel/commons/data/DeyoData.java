package com.deyo.hypixel.commons.data;

import com.deyo.hypixel.libs.DataAPI;
import com.deyo.hypixel.libs.api.DataAPIImpl;
import com.deyo.hypixel.commons.config.ConfigProvider;
import com.deyo.hypixel.commons.redis.RedisConnectionPool;
import com.deyo.hypixel.libs.data.format.JsonFormat;
import com.deyo.hypixel.libs.event.RedisPubSubHandler;
import com.deyo.hypixel.libs.lock.RedisDistributedLock;
import com.deyo.hypixel.libs.storage.RedisDataStorage;
import com.deyo.hypixel.libs.storage.StorageOwnership;
import redis.clients.jedis.JedisPool;

import java.time.Duration;

public final class DeyoData {
    private static final Duration LOCK_TIMEOUT = Duration.ofSeconds(5);

    private static JedisPool pool;
    private static RedisDistributedLock lock;
    private static DataAPI account;
    private static DataAPI profile;

    private DeyoData() {}

    public static synchronized void bootstrap(String redisUri) {
        if (account != null) return;
        pool = RedisConnectionPool.connect(redisUri, RedisConnectionPool.Settings.standard());
        lock = new RedisDistributedLock(pool);
        account = new DataAPIImpl(new RedisDataStorage(pool, "hsb:acct"), new JsonFormat(),
                new RedisPubSubHandler(pool, "hsb-acct"), true, lock, StorageOwnership.BORROWED, LOCK_TIMEOUT);
        profile = new DataAPIImpl(new RedisDataStorage(pool, "hsb:prof"), new JsonFormat(),
                new RedisPubSubHandler(pool, "hsb-prof"), true, lock, StorageOwnership.BORROWED, LOCK_TIMEOUT);
    }

    public static synchronized void shutdown() {
        if (account instanceof DataAPIImpl accountImpl) accountImpl.shutdown();
        if (profile instanceof DataAPIImpl profileImpl) profileImpl.shutdown();
        if (lock != null) lock.close();
        if (pool != null) pool.close();
    }

    private static void ensureBootstrapped() {
        if (account == null) bootstrap(ConfigProvider.settings().getRedisUri());
    }

    public static DataAPI account() {
        ensureBootstrapped();
        return account;
    }

    public static DataAPI profile() {
        ensureBootstrapped();
        return profile;
    }

    public static RedisDistributedLock lock() {
        ensureBootstrapped();
        return lock;
    }

    public static JedisPool jedisPool() {
        ensureBootstrapped();
        return pool;
    }
}
