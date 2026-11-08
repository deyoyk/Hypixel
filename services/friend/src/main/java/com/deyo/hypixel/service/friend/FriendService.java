package com.deyo.hypixel.service.friend;

import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.config.ConfigProvider;
import com.deyo.hypixel.service.generic.SkyBlockService;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.List;

public class FriendService implements SkyBlockService {

    static void main() {
        String mongoUri = ConfigProvider.settings().getMongodb();
        new FriendDatabase(null).connect(mongoUri);

        FriendCache.startExpirationChecker();

        SkyBlockService.init(new FriendService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.FRIEND;
    }

    @Override
    public List<RedisMessageHandler> getEndpoints() {
        return loopThroughPackage("com.deyo.hypixel.service.friend.endpoints", RedisMessageHandler.class).toList();
    }
}
