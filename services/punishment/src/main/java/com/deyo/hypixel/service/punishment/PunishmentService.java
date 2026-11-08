package com.deyo.hypixel.service.punishment;

import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.config.ConfigProvider;
import com.deyo.hypixel.commons.punishment.PunishmentRedis;
import com.deyo.hypixel.service.generic.SkyBlockService;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.List;

public class PunishmentService implements SkyBlockService {

    static void main(String[] args) {
        String mongoUri = ConfigProvider.settings().getMongodb();
        new PunishmentDatabase(null).connect(mongoUri);
        PunishmentRedis.connect(ConfigProvider.settings().getRedisUri());
        SkyBlockService.init(new PunishmentService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.PUNISHMENT;
    }

    @Override
    public List<RedisMessageHandler> getEndpoints() {
        return loopThroughPackage("com.deyo.hypixel.service.punishment.endpoints", RedisMessageHandler.class).toList();
    }
}
