package com.deyo.hypixel.commons.skyblock.auctions;

public enum AuctionsSorting {
    HIGHEST_BID,
    LOWEST_BID,
    ENDING_SOON,
    MOST_BIDS,
    ;

    public AuctionsSorting next() {
        return values()[(ordinal() + 1) % values().length];
    }
}
