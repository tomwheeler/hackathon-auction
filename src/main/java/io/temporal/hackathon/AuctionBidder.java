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

        String name  = System.getenv("USER");

        try {
            Float bid = Float.parseFloat(args[0]);
            client.newUntypedWorkflowStub("car").signal("bid", name, bid);
            System.out.println("Bid placed " + bid);
        } catch (Exception e) {
            System.out.println("Please supply a bid price as the runtime argument");
        }
		System.exit(0);
    }

}
