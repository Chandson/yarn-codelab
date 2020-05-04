package com.chandson.infra;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;

import java.io.IOException;

public class RPCServer implements IRPCInterface{
    public static void main(String args[]) throws IOException {
        Server server = new RPC.Builder(new Configuration())
                .setBindAddress("localhost")//本机地址，也可以换成IP
                .setPort(8888)
                .setInstance(new RPCServer())
                .setProtocol(IRPCInterface.class)
                .build();

        server.start();
    }

    public String echo(String s) {
        return "Server return " + s;
    }
}
