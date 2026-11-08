package com.deyo.hypixel.service.bazaar;

import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.config.ConfigProvider;
import com.deyo.hypixel.service.generic.SkyBlockService;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.List;

public class BazaarService implements SkyBlockService {

    static void main(String[] args) {
        // Connect to MongoDB for orders BEFORE the service starts handling Redis
        // requests, otherwise early messages hit a null OrderDatabase and NPE.
        OrderDatabase.connect(ConfigProvider.settings().getMongodb());

        // Initialize the bazaar market (loads existing orders)
        BazaarMarket.get();
        // load persisted orders
        OrderRepository.loadAll();

        SkyBlockService.init(new BazaarService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.BAZAAR;
    }

    @Override
    public List<RedisMessageHandler> getEndpoints() {
        return loopThroughPackage("com.deyo.hypixel.service.bazaar.endpoints", RedisMessageHandler.class).toList();
    }
}