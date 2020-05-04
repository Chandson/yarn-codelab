package com.chadson.infra;

import com.chandson.infra.PBTestRpcServiceProtos;

interface PBTestRpcService
        extends PBTestRpcServiceProtos.TestProtobufRpcProto.BlockingInterface {
    public static final long versionID = 1;
}
