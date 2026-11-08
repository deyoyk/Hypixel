package com.deyo.hypixel.service.party;

import com.deyo.hypixel.commons.ServiceType;
import com.deyo.hypixel.service.generic.SkyBlockService;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.List;

public class PartyService implements SkyBlockService {

    static void main(String[] args) {
        SkyBlockService.init(new PartyService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.PARTY;
    }

    @Override
    public List<RedisMessageHandler> getEndpoints() {
        return loopThroughPackage("com.deyo.hypixel.service.party.endpoints", RedisMessageHandler.class).toList();
    }
}