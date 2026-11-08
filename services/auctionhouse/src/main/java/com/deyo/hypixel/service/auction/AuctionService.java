package com.deyo.hypixel.service.auction;

import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.config.ConfigProvider;
import com.deyo.hypixel.service.generic.SkyBlockService;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.List;

public class AuctionService implements SkyBlockService {
    public static AuctionsCacheService cacheService;

    static void main(String[] args) {
        new AuctionActiveDatabase("_placeholder").connect(ConfigProvider.settings().getMongodb());
        new AuctionInactiveDatabase("_placeholder").connect(ConfigProvider.settings().getMongodb());

        cacheService = new AuctionsCacheService();

        SkyBlockService.init(new AuctionService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.AUCTION_HOUSE;
    }

    @Override
    public List<RedisMessageHandler> getEndpoints() {
        return loopThroughPackage("com.deyo.hypixel.service.auction.endpoints", RedisMessageHandler.class).toList();
    }
}
