package com.deyo.hypixel.service.election;

import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.config.ConfigProvider;
import com.deyo.hypixel.service.generic.SkyBlockService;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.List;

public class ElectionService implements SkyBlockService {

    public static void main(String[] args) {
        String mongoUri = ConfigProvider.settings().getMongodb();
        new ElectionDatabase(null).connect(mongoUri);
        SkyBlockService.init(new ElectionService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.ELECTION;
    }

    @Override
    public List<RedisMessageHandler> getEndpoints() {
        return loopThroughPackage("com.deyo.hypixel.service.election.endpoints", RedisMessageHandler.class).toList();
    }
}
