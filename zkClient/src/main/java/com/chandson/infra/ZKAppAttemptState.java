package com.chandson.infra;

import org.apache.hadoop.io.DataInputByteBuffer;
import org.apache.hadoop.security.Credentials;
import org.apache.hadoop.security.token.Token;
import org.apache.hadoop.security.token.TokenIdentifier;
import org.apache.hadoop.yarn.proto.YarnServerResourceManagerRecoveryProtos;
import org.apache.hadoop.yarn.server.resourcemanager.recovery.records.impl.pb.ApplicationAttemptStateDataPBImpl;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ZKAppAttemptState {
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

                String formats = "yyyy-MM-dd HH:mm:ss";

                ApplicationAttemptStateDataPBImpl attemptState =
                        new ApplicationAttemptStateDataPBImpl(
                                YarnServerResourceManagerRecoveryProtos.ApplicationAttemptStateDataProto.parseFrom(zoo_data));

                System.out.println("attemptId:" + attemptState.getAttemptId());
                System.out.println("startTime: " + new SimpleDateFormat(formats, Locale.CHINA).format(new Date(attemptState.getStartTime())));
                System.out.println("finishTime: " + new SimpleDateFormat(formats, Locale.CHINA).format(new Date(attemptState.getFinishTime())));
                System.out.println("masterContainer: " + attemptState.getMasterContainer());

                System.out.println("appattempt tokens:");
                Credentials credentials = new Credentials();
                DataInputByteBuffer dibb = new DataInputByteBuffer();
                ByteBuffer tokens = attemptState.getAppAttemptTokens();
                if (tokens != null) {
                    dibb.reset(tokens);
                    credentials.readTokenStorageStream(dibb);
                    tokens.rewind();
                }
                // debug token here
                for(Token<? extends TokenIdentifier> token: credentials.getAllTokens()) {
                    System.out.println(token);
                }

                System.out.println("Hello,hn!");
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
