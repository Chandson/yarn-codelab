package com.chadson.infra;

import com.chandson.infra.PBTestProtos;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.ProtobufRpcEngine;
import org.apache.hadoop.ipc.RPC;
import java.net.InetSocketAddress;

public class PBClient {
    public static void main(String args[]) throws Exception {
        // Set RPC engine to protobuf RPC engine
        Configuration conf = new Configuration();
        RPC.setProtocolEngine(conf, PBTestRpcService.class, ProtobufRpcEngine.class);
        PBTestRpcService client = RPC.getProxy(PBTestRpcService.class, 1,
                new InetSocketAddress("localhost", 8888), conf);
        PBTestProtos.EchoRequestProto echoRequest = PBTestProtos.EchoRequestProto.newBuilder()
                .setMessage("hello,hn!").build();
        PBTestProtos.EchoResponseProto echoResponse = client.echo(null, echoRequest);

        System.out.println(echoResponse.getMessage());
    }
}
