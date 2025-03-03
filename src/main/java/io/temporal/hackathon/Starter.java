package io.temporal.hackathon;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class Starter {

    public static void main(String[] args) {
        String item = "car";

        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient workflowClient = WorkflowClient.newInstance(service);

        WorkflowOptions options = WorkflowOptions.newBuilder().setWorkflowId(item).setTaskQueue("auction").build();
        Auction auction = workflowClient.newWorkflowStub(Auction.class, options);

        int amount = auction.startAuction(item);

        System.out.println("Auction complete, price was $" + amount);
        System.exit(0);
    }
}
