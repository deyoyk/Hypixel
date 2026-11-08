package com.deyo.hypixel.service.darkauction.endpoints;

import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.darkauction.GetAuctionStateProtocol;
import com.deyo.hypixel.service.darkauction.DarkAuctionService;
import com.deyo.hypixel.service.darkauction.DarkAuctionState;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;

import java.util.List;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class EndpointGetAuctionState implements RedisMessageHandler<
        GetAuctionStateProtocol.GetAuctionStateMessage,
        GetAuctionStateProtocol.GetAuctionStateResponse> {

    @Override
    public RedisProtocol<GetAuctionStateProtocol.GetAuctionStateMessage, GetAuctionStateProtocol.GetAuctionStateResponse> protocol() {
        return new GetAuctionStateProtocol();
    }

    @Override
    public GetAuctionStateProtocol.GetAuctionStateResponse handle(GetAuctionStateProtocol.GetAuctionStateMessage msg, RedisMessageContext context) {

        DarkAuctionState auction = DarkAuctionService.getCurrentAuction();

        if (auction == null) {
            return new GetAuctionStateProtocol.GetAuctionStateResponse(
                    false, null, null, 0, null, 0, null, null, List.of(), true, null
            );
        }

        return new GetAuctionStateProtocol.GetAuctionStateResponse(
                true,
                auction.getAuctionId(),
                auction.getPhase(),
                auction.getCurrentRound(),
                auction.getCurrentItem() != null ? auction.getCurrentItem().name() : null,
                auction.getCurrentBid(),
                auction.getHighestBidderId(),
                auction.getHighestBidderName(),
                auction.getRoundItemNames(),
                true,
                null
        );
    }
}
