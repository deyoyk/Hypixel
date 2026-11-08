package com.deyo.hypixel.service.generic.redis;

import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.redis.RedisClient;
import com.deyo.hypixel.commons.redis.RedisEndpoint;
import com.deyo.hypixel.libs.redisapi.api.RedisAPI;

public class ServiceRedisManager {
    public static void connect(String URI, ServiceType type) {
        RedisAPI.generateInstance(URI);
        RedisAPI.getInstance().setFilterId(type.name());
        RedisClient.identify(RedisEndpoint.service(type));
    }
}
