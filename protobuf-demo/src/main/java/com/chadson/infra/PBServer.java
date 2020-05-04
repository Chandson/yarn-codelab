package com.chadson.infra;

import com.chandson.infra.PBTestRpcServiceProtos;
import com.google.protobuf.BlockingService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.ProtobufRpcEngine;
import org.apache.hadoop.ipc.RPC;

import java.io.IOException;

public class PBServer {
    public final static String ADDRESS = "localhost";
    public final static int PORT = 8888;

    public static void main(String args[]) throws IOException {
        // Set RPC engine to protobuf RPC engine
        Configuration conf = new Configuration();
        RPC.setProtocolEngine(conf, PBTestRpcService.class, ProtobufRpcEngine.class);

        PBServerImpl serverImpl = new PBServerImpl();
        BlockingService service = PBTestRpcServiceProtos.TestProtobufRpcProto
                .newReflectiveBlockingService(serverImpl);
        RPC.Server server = new RPC.Builder(conf).setProtocol(PBTestRpcService.class)
                .setInstance(service).setBindAddress(ADDRESS).setPort(PORT).build();
        server.start();
        System.out.println("Begin to offer service!");
    }
}
