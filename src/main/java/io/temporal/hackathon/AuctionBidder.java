package io.temporal.hackathon;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.common.converter.CodecDataConverter;
import io.temporal.common.converter.DefaultDataConverter;
import io.temporal.hackathon.codec.SecurePayloadCodec;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.hackathon.client.ClientProvider;

import java.io.IOException;
import java.util.Optional;
import java.util.List; 

public class AuctionBidder {

    public static void main(String[] args) throws IOException {
        WorkflowClient client = ClientProvider.getClient();
        String name  = System.getenv("USER");

        try {
            long bid = Long.parseLong(args[0]);
            client.newUntypedWorkflowStub("car").signal("bid", name, bid);
            System.out.println("Bid placed " + bid);
        } catch (Exception e) {
            System.out.println("Please supply a bid price as the runtime argument");

            System.out.println(e);
        }
		    System.exit(0);
    }
}
