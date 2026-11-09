package com.deyo.hypixel.service.bazaar;

import com.deyo.hypixel.commons.protocol.objects.bazaar.BazaarTransactionPushProtocol;
import com.deyo.hypixel.commons.protocol.objects.bazaar.BazaarTransactionPushProtocol.Response;
import com.deyo.hypixel.commons.skyblock.bazaar.BazaarTransaction;
import com.deyo.hypixel.commons.skyblock.bazaar.BuyOrderRefundTransaction;
import com.deyo.hypixel.commons.skyblock.bazaar.SuccessfulBazaarTransaction;
import com.deyo.hypixel.commons.skyblock.bazaar.OrderExpiredBazaarTransaction;
import com.deyo.hypixel.commons.redis.RedisClient;
import org.tinylog.Logger;

import java.util.Map;
import java.util.UUID;

public class BazaarPropagator {

    private static final BazaarTransactionPushProtocol PROTOCOL = new BazaarTransactionPushProtocol();

    public void propagate(BazaarTransaction tx) {
        Logger.debug("Propagating transaction {}", tx.getClass().getSimpleName());

        var request = new BazaarTransactionPushProtocol.Request(
                tx.getClass().getSimpleName(),
                tx.toJSON().toString()
        );

        RedisClient.requestAllServersFromService(PROTOCOL, request, 5000)
                .thenAccept(responses -> handleServerResponses(tx, responses))
                .exceptionally(throwable -> {
                    Logger.error(throwable, "Failed to get responses from servers for transaction");
                    return null;
                });
    }

    private void handleServerResponses(BazaarTransaction tx, Map<UUID, Response> responses) {
        switch (tx) {
            case SuccessfulBazaarTransaction success -> handleSuccessfulTransactionResponses(success, responses);
            case OrderExpiredBazaarTransaction expired -> handleOwnerTransactionResponses(
                    expired.owner(), expired.ownerProfile(), expired, responses);
            case BuyOrderRefundTransaction refund -> handleOwnerTransactionResponses(
                    refund.owner(), refund.ownerProfile(), refund, responses);
            default -> Logger.warn("Unknown transaction type for response handling: {}", tx.getClass().getSimpleName());
        }
    }

    private void handleSuccessfulTransactionResponses(SuccessfulBazaarTransaction tx, Map<UUID, Response> responses) {
        boolean buyerHandled = false;
        boolean sellerHandled = false;

        for (Response response : responses.values()) {
            if (response != null && response.success()) {
                buyerHandled |= response.buyerHandled();
                sellerHandled |= response.sellerHandled();
            }
        }

        if (!buyerHandled) {
            Logger.info("Buyer {} not handled by any server — storing as pending", tx.buyer());
            PendingTransactionsDatabase.storePendingTransaction(tx.buyer(), tx.buyerProfile(), tx);
        }

        if (!sellerHandled) {
            Logger.info("Seller {} not handled by any server — storing as pending", tx.seller());
            PendingTransactionsDatabase.storePendingTransaction(tx.seller(), tx.sellerProfile(), tx);
        }

        Logger.debug("Transaction handled — buyer={}, seller={}", buyerHandled, sellerHandled);
    }

    private void handleOwnerTransactionResponses(UUID owner, UUID ownerProfile,
                                                 BazaarTransaction tx, Map<UUID, Response> responses) {
        boolean ownerHandled = false;

        for (Response response : responses.values()) {
            if (response != null && response.success()) {
                ownerHandled |= response.buyerHandled();
            }
        }

        if (!ownerHandled) {
            Logger.info("Owner {} not handled by any server — storing as pending", owner);
            PendingTransactionsDatabase.storePendingTransaction(owner, ownerProfile, tx);
        }

        Logger.debug("Owner transaction handled — owner={}", ownerHandled);
    }
}
