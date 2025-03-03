package io.temporal.hackathon.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.util.JsonFormat;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.plugin.bundled.CorsPluginConfig;
import io.temporal.api.common.v1.Payload;
import io.temporal.api.common.v1.Payloads;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CodecServer {

    private static final Logger logger = LoggerFactory.getLogger(CodecServer.class);

    private final int port;
    private final Javalin server;


    public CodecServer(int port) {
        logger.info("Creating new CodecServer instance, with port={}", port);

        this.port = port;

        server = Javalin.create(config -> {
            config.showJavalinBanner = false;
            // Enable CORS for all origins
            // In production it should be configured with only the proper Temporal UI DNS
            config.bundledPlugins.enableCors(cors -> cors.addRule(CorsPluginConfig.CorsRule::anyHost));
        });
        server.post("/decode", new DecodeHandler());
    }

    public void start() {
        logger.info("Starting CodecServer on port {}", port);
        server.start("localhost", port);
    }

    private class DecodeHandler implements Handler {


        @Override
        public void handle(Context ctx) throws Exception {

            logger.debug("CodecServer handling 'decode' request");

            try {

                final SecurePayloadCodec securePayloadCodec = new SecurePayloadCodec();
            String body = ctx.body();

            Payloads.Builder incomingPayloads = Payloads.newBuilder();
            JsonFormat.parser().merge(body, incomingPayloads);

            List<Payload> incomingPayloadsList = incomingPayloads.build().getPayloadsList();
            List<Payload> outgoingPayloadsList = securePayloadCodec.decode(incomingPayloadsList);


            final String responseBody = JsonFormat.printer()
                    .print(Payloads.newBuilder()
                            .addAllPayloads(outgoingPayloadsList)
                            .build());

                sendResult(ctx, responseBody);
            } catch (Exception e) {
                sendError(ctx, e.getMessage());
            }
        }
    }

    private void sendResult(Context ctx, String data) throws JsonProcessingException {
        logger.debug("CodecServer sending result");

        String result = new ObjectMapper().writeValueAsString(data);
        ctx.status(200);
        ctx.result(result);
    }

    private void sendError(Context ctx, String data) throws JsonProcessingException {
        logger.debug("CodecServer sending error");

        String result = new ObjectMapper().writeValueAsString(data);
        ctx.status(500);
        ctx.result(result);
    }
}