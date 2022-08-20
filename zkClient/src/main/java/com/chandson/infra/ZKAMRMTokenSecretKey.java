package com.chandson.infra;

import java.io.IOException;
import org.apache.hadoop.yarn.proto.YarnServerResourceManagerRecoveryProtos;
import org.apache.hadoop.yarn.server.resourcemanager.recovery.records.impl.pb.AMRMTokenSecretManagerStatePBImpl;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

public class ZKAMRMTokenSecretKey {
    public static void main(String args[]) throws IOException {
        if (args.length != 2) {
            System.err.println("Please input hostPort and zkPath");
            System.exit(1);
        }
        String hostPort = args[0];
        String zkPath = args[1];
        ZooKeeper zk = new ZooKeeper(hostPort, 2000, null);
        if (zk != null) {
            try {
                byte[] zoo_data = zk.getData(zkPath, true, null);
                AMRMTokenSecretManagerStatePBImpl amrmTokenSecretManagerStatePB =
                    new AMRMTokenSecretManagerStatePBImpl(
                        YarnServerResourceManagerRecoveryProtos.AMRMTokenSecretManagerStateProto.parseFrom(zoo_data));

                System.out.println(amrmTokenSecretManagerStatePB.getCurrentMasterKey());
                System.out.println(amrmTokenSecretManagerStatePB.getNextMasterKey());
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
