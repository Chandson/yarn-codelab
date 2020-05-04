package com.chandson.infra;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import java.net.InetSocketAddress;

public class RPCClient {
    public static void main(String[] args) throws Exception {
        IRPCInterface proxy = RPC.getProxy(IRPCInterface.class, 1,//指明调用接口的哪个rpc版本（基本上无用处,版本已经在接口文件中指明了)
                new InetSocketAddress("localhost", 8888), new Configuration());

        String s = proxy.echo("hello,hn!");

        System.out.println("Get from server: " + s);
    }
}
