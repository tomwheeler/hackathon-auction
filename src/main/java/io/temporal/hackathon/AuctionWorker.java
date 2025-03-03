package io.temporal.hackathon;

import io.temporal.client.WorkflowClient;
import io.temporal.hackathon.domain.temporal.AuctionWorkflowImpl;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.hackathon.client.ClientProvider;
import java.io.IOException;

public class AuctionWorker {

    public static void main(String[] args) throws IOException {
        WorkflowClient client = ClientProvider.getClient();
        WorkerFactory factory = WorkerFactory.newInstance(client);

        Worker worker = factory.newWorker("auction"); // Task Queue name

        worker.registerWorkflowImplementationTypes(AuctionWorkflowImpl.class);
        factory.start();
        System.out.println("Worker started");
    }
}
