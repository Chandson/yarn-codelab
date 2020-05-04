package com.chadson.infra;

import com.chandson.infra.PBTestProtos;
import com.google.protobuf.RpcController;
import com.google.protobuf.ServiceException;
import org.apache.hadoop.ipc.RpcServerException;
import org.apache.hadoop.ipc.Server;

import java.net.InetAddress;
import java.net.URISyntaxException;

class PBServerImpl implements PBTestRpcService {

    public PBTestProtos.EmptyResponseProto ping(RpcController unused,
                                                PBTestProtos.EmptyRequestProto request) {
        return PBTestProtos.EmptyResponseProto.newBuilder().build();
    }

    public PBTestProtos.EchoResponseProto echo(RpcController unused, PBTestProtos.EchoRequestProto request) {
        return PBTestProtos.EchoResponseProto.newBuilder().setMessage("Echo: " + request.getMessage())
                .build();
    }

    public PBTestProtos.EmptyResponseProto error(RpcController unused,
                                                 PBTestProtos.EmptyRequestProto request) throws ServiceException {
        throw new ServiceException("error", new RpcServerException("error"));
    }

    public PBTestProtos.EmptyResponseProto error2(RpcController unused,
                                                  PBTestProtos.EmptyRequestProto request) throws ServiceException {
        throw new ServiceException("error", new URISyntaxException("",
                "testException"));
    }

    public PBTestProtos.GetProxyAddressResponseProto getProxyAddress(
            RpcController controller,
            PBTestProtos.GetProxyAddressRequestProto request) {
        InetAddress address = Server.getRemoteIp();
        if (address != null) {
            // String proxyAddress = address.getAddress() + ":" + address.getPort();
            return PBTestProtos.GetProxyAddressResponseProto.newBuilder()
                    .setMessage(address.toString()).build();
        } else {
            return null;
        }
    }
}
