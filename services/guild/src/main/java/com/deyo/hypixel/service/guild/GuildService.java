package com.deyo.hypixel.service.guild;

import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.config.ConfigProvider;
import com.deyo.hypixel.service.generic.SkyBlockService;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.List;

public class GuildService implements SkyBlockService {

    static void main() {
        String mongoUri = ConfigProvider.settings().getMongodb();
        new GuildDatabase(null).connect(mongoUri);

        SkyBlockService.init(new GuildService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.GUILD;
    }

    @Override
    public List<RedisMessageHandler> getEndpoints() {
        return loopThroughPackage("com.deyo.hypixel.service.guild.endpoints", RedisMessageHandler.class).toList();
    }
}
