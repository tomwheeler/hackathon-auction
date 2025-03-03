package io.temporal.hackathon;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

import java.util.Optional;

public class AuctionBidder {

    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);

        client.newUntypedWorkflowStub("car").signal("bid", "user", 100);
        System.out.println("Bid placed");
    }
}
