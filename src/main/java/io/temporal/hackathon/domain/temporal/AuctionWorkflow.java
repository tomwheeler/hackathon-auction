package io.temporal.hackathon.domain.temporal;

import io.temporal.hackathon.domain.auction.AuctionStats;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;


@WorkflowInterface
public interface AuctionWorkflow {

    @WorkflowMethod
    long startAuction(String auctionId);

    @SignalMethod
    void bid(String userId, Long amount);

    @QueryMethod
    AuctionStats getStats();

    @SignalMethod
    void end();
}
