package com.deyo.hypixel.service.darkauction.endpoints;

import com.deyo.hypixel.commons.skyblock.auctions.DarkAuctionPhase;
import com.deyo.hypixel.commons.protocol.RedisProtocol;
import com.deyo.hypixel.commons.protocol.objects.darkauction.PlaceBidProtocol;
import com.deyo.hypixel.service.darkauction.DarkAuctionScheduler;
import com.deyo.hypixel.service.darkauction.DarkAuctionService;
import com.deyo.hypixel.service.darkauction.DarkAuctionState;
import com.deyo.hypixel.commons.redis.RedisMessageHandler;
import org.tinylog.Logger;

import java.util.UUID;
import com.deyo.hypixel.commons.redis.RedisMessageContext;

public class EndpointPlaceBid implements RedisMessageHandler<
        PlaceBidProtocol.PlaceBidMessage,
        PlaceBidProtocol.PlaceBidResponse> {

    @Override
    public RedisProtocol<PlaceBidProtocol.PlaceBidMessage, PlaceBidProtocol.PlaceBidResponse> protocol() {
        return new PlaceBidProtocol();
    }

    @Override
    public PlaceBidProtocol.PlaceBidResponse handle(PlaceBidProtocol.PlaceBidMessage msg, RedisMessageContext context) {

        DarkAuctionState auction = DarkAuctionService.getCurrentAuction();

        // Check if there's an active auction
        if (auction == null) {
            Logger.warn("Bid rejected: No active auction");
            return new PlaceBidProtocol.PlaceBidResponse(false, "No active auction", null);
        }

        // Check if auction ID matches
        if (!auction.getAuctionId().equals(msg.auctionId())) {
            Logger.warn("Bid rejected: Auction ID mismatch");
            return new PlaceBidProtocol.PlaceBidResponse(false, "Invalid auction ID", null);
        }

        // Check if we're in bidding phase
        if (auction.getPhase() != DarkAuctionPhase.BIDDING) {
            Logger.warn("Bid rejected: Not in bidding phase (current: {})", auction.getPhase());
            return new PlaceBidProtocol.PlaceBidResponse(false, "Auction is not in bidding phase", null);
        }

        // SYNCHRONIZED to prevent race conditions
        synchronized (auction) {
            // Validate bid is higher than current
            if (msg.bidAmount() <= auction.getCurrentBid()) {
                Logger.info("Bid rejected: {} tried to bid {} but current bid is {}",
                        msg.playerName(), msg.bidAmount(), auction.getCurrentBid());
                return new PlaceBidProtocol.PlaceBidResponse(false,
                        "Bid too low - someone else bid first! Current bid is " + auction.getCurrentBid(), null);
            }

            // If there's a previous bidder, queue their refund
            UUID previousBidder = auction.getHighestBidderId();
            long previousBid = auction.getCurrentBid();

            if (previousBidder != null && previousBid > 0) {
                Logger.info("Queueing refund of {} to previous bidder {}", previousBid, previousBidder);
                auction.removeEscrow(previousBidder);
            }

            // Update state with new bid
            auction.setCurrentBid(msg.bidAmount());
            auction.setHighestBidderId(msg.playerId());
            auction.setHighestBidderName(msg.playerName());
            auction.setEscrowedAmount(msg.playerId(), msg.bidAmount());

            // Record in bid history for reconciliation
            auction.recordBid(msg.playerId(), msg.playerName(), msg.bidAmount());

            Logger.info("Bid accepted: {} bid {} coins for {}",
                    msg.playerName(), msg.bidAmount(), auction.getCurrentItem());

            // Reset countdown timer
            DarkAuctionScheduler.resetRoundTimer(auction);

            // Broadcast BID_PLACED with refund info
            DarkAuctionScheduler.broadcastBidPlaced(auction, previousBidder, previousBid);

            return new PlaceBidProtocol.PlaceBidResponse(true, "Bid accepted", null);
        }
    }
}
