package com.deyo.hypixel.service.store;

import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.commons.config.ConfigProvider;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import com.deyo.hypixel.service.generic.SkyBlockService;

import java.util.List;

public class StoreService implements SkyBlockService {
    private final PurchaseFulfillmentWorker worker;

    public StoreService(PurchaseFulfillmentWorker worker) {
        this.worker = worker;
    }

    static void main() {
        String mongoUri = ConfigProvider.settings().getMongodb();
        StoreDatabase database = (StoreDatabase) new StoreDatabase(null).connect(mongoUri);

        SkyBlockService.init(new StoreService(new PurchaseFulfillmentWorker(database)));
    }

    @Override
    public ServiceType getType() {
        return ServiceType.STORE;
    }

    @Override
    public List<RedisMessageHandler> getEndpoints() {
        return List.of();
    }

    @Override
    public void onReady() {
        worker.start();
    }
}
