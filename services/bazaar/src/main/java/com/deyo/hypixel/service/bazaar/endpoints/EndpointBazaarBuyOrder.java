package com.deyo.hypixel.service.bazaar.endpoints;

import com.deyo.hypixel.commons.protocol.objects.bazaar.BazaarBuyProtocol;
import com.deyo.hypixel.service.bazaar.BazaarMarket;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;

import java.util.UUID;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class EndpointBazaarBuyOrder implements RedisMessageHandler<
        BazaarBuyProtocol.BazaarBuyMessage,
        BazaarBuyProtocol.BazaarBuyResponse> {

    @Override
    public BazaarBuyProtocol protocol() {
        return new BazaarBuyProtocol();
    }

    @Override
    public BazaarBuyProtocol.BazaarBuyResponse handle(BazaarBuyProtocol.BazaarBuyMessage msg, RedisMessageContext context) {

        String itemName    = msg.itemName();
        UUID   playerUUID  = msg.playerUUID();
        UUID   profileUUID = msg.profileUUID();
        double price       = msg.price();
        double amount      = msg.amount();

        try {
            BazaarMarket.get().submitBuy(itemName, playerUUID, profileUUID, price, amount);
            Logger.info("Buy order submitted for {} by {} (profile: {}) — price={}, amount={}",
                    itemName, playerUUID, profileUUID, price, amount);
            return new BazaarBuyProtocol.BazaarBuyResponse(true, null);
        } catch (Exception e) {
            Logger.error(e, "Failed to submit buy order for {} by {}", itemName, playerUUID);
            return new BazaarBuyProtocol.BazaarBuyResponse(false, "Buy order failed");
        }
    }
}
