package com.deyo.hypixel.service.itemtracker;

import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.config.ConfigProvider;
import com.deyo.hypixel.service.generic.SkyBlockService;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.List;

public class ItemTrackerService implements SkyBlockService {
    static void main(String[] args) {
        // Connect the DB before the service starts handling Redis requests,
        // otherwise early messages hit a null collection and NPE.
        TrackedItemsDatabase.connect(ConfigProvider.settings().getMongodb());

        SkyBlockService.init(new ItemTrackerService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.ITEM_TRACKER;
    }

    @Override
    public List<RedisMessageHandler> getEndpoints() {
        return loopThroughPackage("com.deyo.hypixel.service.itemtracker.endpoints", RedisMessageHandler.class).toList();
    }
}
