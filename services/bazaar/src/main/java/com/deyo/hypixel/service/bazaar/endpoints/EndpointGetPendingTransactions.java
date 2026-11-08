package com.deyo.hypixel.service.bazaar.endpoints;

import com.deyo.hypixel.commons.protocol.objects.bazaar.BazaarGetPendingTransactionsProtocol;
import com.deyo.hypixel.commons.protocol.objects.bazaar.BazaarGetPendingTransactionsProtocol.BazaarGetPendingTransactionsMessage;
import com.deyo.hypixel.commons.protocol.objects.bazaar.BazaarGetPendingTransactionsProtocol.BazaarGetPendingTransactionsResponse;
import com.deyo.hypixel.commons.protocol.objects.bazaar.BazaarGetPendingTransactionsProtocol.PendingTransactionInfo;
import com.deyo.hypixel.service.bazaar.PendingTransactionsDatabase;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;

import java.util.List;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class EndpointGetPendingTransactions implements RedisMessageHandler<
        BazaarGetPendingTransactionsMessage,
        BazaarGetPendingTransactionsResponse> {

    @Override
    public BazaarGetPendingTransactionsProtocol protocol() {
        return new BazaarGetPendingTransactionsProtocol();
    }

    @Override
    public BazaarGetPendingTransactionsResponse handle(BazaarGetPendingTransactionsMessage msg, RedisMessageContext context) {

        List<PendingTransactionsDatabase.PendingTransaction> pendingTransactions =
                PendingTransactionsDatabase.getPendingTransactions(msg.playerUUID(), msg.profileUUID());

        List<PendingTransactionInfo> transactionInfos = pendingTransactions.stream()
                .map(pt -> new PendingTransactionInfo(
                        pt.getId(),
                        pt.getTransaction().getClass().getSimpleName(),
                        pt.getTransaction().toJSON().toMap(),
                        pt.getCreatedAt()
                ))
                .toList();

        Logger.debug("Retrieved {} pending transactions for player {} on profile {}",
                transactionInfos.size(), msg.playerUUID(), msg.profileUUID());

        return new BazaarGetPendingTransactionsResponse(transactionInfos, true, null);
    }
}
