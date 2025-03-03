package io.temporal.hackathon;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.common.converter.CodecDataConverter;
import io.temporal.common.converter.DefaultDataConverter;
import io.temporal.hackathon.codec.SecurePayloadCodec;
import io.temporal.hackathon.domain.temporal.AuctionWorkflowImpl;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

import java.util.List;

public class AuctionWorker {

    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service, WorkflowClientOptions.newBuilder()
                .setDataConverter(
                        new CodecDataConverter(
                                DefaultDataConverter.newDefaultInstance(),
                                List.of(new SecurePayloadCodec()), true))
                .build());
        WorkerFactory factory = WorkerFactory.newInstance(client);

        Worker worker = factory.newWorker("auction"); // Task Queue name

        worker.registerWorkflowImplementationTypes(AuctionWorkflowImpl.class);
        factory.start();
        System.out.println("Worker started");
    }
}
