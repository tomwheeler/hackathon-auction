package io.temporal.hackathon;

import io.temporal.hackathon.codec.CodecServer;

public class CodecStarter {

    public static void main(String[] args) {

        CodecServer codecServer = new CodecServer(8000);
        codecServer.start();

    }
}
