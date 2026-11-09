package com.deyo.hypixel.service.bazaar.endpoints;

import com.mongodb.client.model.Filters;
import com.deyo.hypixel.commons.protocol.objects.bazaar.BazaarCancelProtocol;
import com.deyo.hypixel.commons.protocol.objects.bazaar.BazaarCancelProtocol.CancelMessage;
import com.deyo.hypixel.commons.protocol.objects.bazaar.BazaarCancelProtocol.CancelResponse;
import com.deyo.hypixel.service.bazaar.BazaarMarket;
import com.deyo.hypixel.service.bazaar.OrderDatabase;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class EndpointCancelBazaarOrder implements RedisMessageHandler<
        CancelMessage, CancelResponse> {

    @Override
    public BazaarCancelProtocol protocol() {
        return new BazaarCancelProtocol();
    }

    @Override
    public CancelResponse handle(CancelMessage msg, RedisMessageContext context) {
        var result = OrderDatabase.ordersCollection.deleteOne(
                Filters.and(
                        Filters.eq("_id", msg.orderId().toString()),
                        Filters.eq("owner", msg.playerUuid().toString()),
                        Filters.eq("profileUuid", msg.profileUuid().toString())
                )
        );
        boolean success = result.getDeletedCount() > 0;
        if (success) {
            BazaarMarket.get().submitDelete(msg.orderId(), msg.playerUuid(), msg.profileUuid());
            Logger.info("Deleted order {} for player {} and profile {}",
                    msg.orderId(), msg.playerUuid(), msg.profileUuid());
        }

        return new CancelResponse(success, success ? null : "Cancel failed");
    }
}
