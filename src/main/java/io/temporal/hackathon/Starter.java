package io.temporal.hackathon;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.hackathon.domain.temporal.AuctionWorkflow;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class Starter {

    public static void main(String[] args) {
        String item = "car";

        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient workflowClient = WorkflowClient.newInstance(service);

        WorkflowOptions options = WorkflowOptions.newBuilder().setWorkflowId(item).setTaskQueue("auction").build();
        AuctionWorkflow auction = workflowClient.newWorkflowStub(AuctionWorkflow.class, options);

        long amount = auction.startAuction(item);

        System.out.println("Auction complete, price was $" + amount);
        System.exit(0);
    }
}
