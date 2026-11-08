package com.deyo.hypixel.service.darkauction;

import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.service.generic.SkyBlockService;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.List;

public class DarkAuctionService implements SkyBlockService {
    private static DarkAuctionState currentAuction = null;

    public static void main(String[] args) {
        DarkAuctionScheduler.start();

        SkyBlockService.init(new DarkAuctionService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.DARK_AUCTION;
    }

    @Override
    public List<RedisMessageHandler> getEndpoints() {
        return loopThroughPackage("com.deyo.hypixel.service.darkauction.endpoints", RedisMessageHandler.class).toList();
    }

    public static DarkAuctionState getCurrentAuction() {
        return currentAuction;
    }

    public static void setCurrentAuction(DarkAuctionState auction) {
        currentAuction = auction;
    }

    public static boolean hasActiveAuction() {
        return currentAuction != null;
    }
}
