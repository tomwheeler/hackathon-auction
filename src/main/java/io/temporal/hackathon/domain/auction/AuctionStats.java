package io.temporal.hackathon.domain.auction;

import io.temporal.hackathon.domain.bid.Bid;

import java.util.List;


public record AuctionStats(
        String auctionId,
        AuctionState state,
        List<Bid> previousBids
) {
}


