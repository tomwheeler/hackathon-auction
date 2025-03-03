package io.temporal.hackathon;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.converter.CodecDataConverter;
import io.temporal.common.converter.DefaultDataConverter;
import io.temporal.hackathon.codec.SecurePayloadCodec;
import io.temporal.hackathon.domain.temporal.AuctionWorkflow;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.hackathon.client.ClientProvider;
import java.io.IOException;

import java.util.List;

public class Starter {

    public static void main(String[] args) throws IOException {
        String item = "car";

        WorkflowClient client = ClientProvider.getClient();
        WorkflowOptions options = WorkflowOptions.newBuilder().setWorkflowId(item).setTaskQueue("auction").build();
        AuctionWorkflow auction = client.newWorkflowStub(AuctionWorkflow.class, options);

        long amount = auction.startAuction(item);

        System.out.println("Auction complete, price was $" + amount);
        System.exit(0);
    }
}
