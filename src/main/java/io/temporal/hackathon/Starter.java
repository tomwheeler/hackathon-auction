package io.temporal.hackathon;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.converter.CodecDataConverter;
import io.temporal.common.converter.DefaultDataConverter;
import io.temporal.hackathon.codec.SecurePayloadCodec;
import io.temporal.hackathon.domain.temporal.AuctionWorkflow;
import io.temporal.serviceclient.WorkflowServiceStubs;

import java.util.List;

public class Starter {

    public static void main(String[] args) {
        String item = "car";

        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service, WorkflowClientOptions.newBuilder()
                .setDataConverter(
                        new CodecDataConverter(
                                DefaultDataConverter.newDefaultInstance(),
                                List.of(new SecurePayloadCodec()), true))
                .build());

        WorkflowOptions options = WorkflowOptions.newBuilder().setWorkflowId(item).setTaskQueue("auction").build();
        AuctionWorkflow auction = client.newWorkflowStub(AuctionWorkflow.class, options);

        long amount = auction.startAuction(item);

        System.out.println("Auction complete, price was $" + amount);
        System.exit(0);
    }
}
