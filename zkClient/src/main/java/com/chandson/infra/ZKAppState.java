package com.chandson.infra;

import org.apache.hadoop.hdfs.security.token.delegation.DelegationTokenIdentifier;
import org.apache.hadoop.io.DataInputByteBuffer;
import org.apache.hadoop.security.Credentials;
import org.apache.hadoop.security.token.Token;
import org.apache.hadoop.security.token.TokenIdentifier;
import org.apache.hadoop.yarn.proto.YarnServerResourceManagerRecoveryProtos;
import org.apache.hadoop.yarn.server.resourcemanager.recovery.records.impl.pb.ApplicationStateDataPBImpl;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ZKAppState {
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

                ApplicationStateDataPBImpl appState =
                        new ApplicationStateDataPBImpl(
                                YarnServerResourceManagerRecoveryProtos.ApplicationStateDataProto.parseFrom(zoo_data));
                String formats = "yyyy-MM-dd HH:mm:ss";
                System.out.println("submitTime: " + new SimpleDateFormat(formats, Locale.CHINA).format(new Date(appState.getSubmitTime())));
                System.out.println("startTime: " + new SimpleDateFormat(formats, Locale.CHINA).format(new Date(appState.getStartTime())));
                System.out.println("finishTime: " + new SimpleDateFormat(formats, Locale.CHINA).format(new Date(appState.getFinishTime())));
                System.out.println("user:" + appState.getUser());
                System.out.println("state: " + appState.getState());

                // container context
                System.out.println("app acl: " + appState.getApplicationSubmissionContext().getAMContainerSpec().getApplicationACLs());
                System.out.println("app command: " + appState.getApplicationSubmissionContext().getAMContainerSpec().getCommands());
                System.out.println("app env: " + appState.getApplicationSubmissionContext().getAMContainerSpec().getEnvironment());
                System.out.println("app local resource: " + appState.getApplicationSubmissionContext().getAMContainerSpec().getLocalResources());
                System.out.println("app service data: " + appState.getApplicationSubmissionContext().getAMContainerSpec().getServiceData());

                // container context token
                System.out.println("app tokens:");
                Credentials credentials = new Credentials();
                DataInputByteBuffer dibb = new DataInputByteBuffer();
                ByteBuffer tokens = appState.getApplicationSubmissionContext().getAMContainerSpec().getTokens();
                if (tokens != null) {
                    dibb.reset(tokens);
                    credentials.readTokenStorageStream(dibb);
                    tokens.rewind();
                }
                // debug token here
                for(Token<? extends TokenIdentifier> token: credentials.getAllTokens()) {
                    System.out.println("kind:" + token.getKind());
                    if(token.getKind().toString().equals("HDFS_DELEGATION_TOKEN")) {
                        Token<DelegationTokenIdentifier> token2 = (Token<DelegationTokenIdentifier>) token;
                        System.out.println("owner:" +  token2.decodeIdentifier().getOwner());
                        System.out.println("renewer:" + token2.decodeIdentifier().getRenewer());
                        System.out.println("realuser:" +  token2.decodeIdentifier().getRealUser());
                        System.out.println("issuedate:" +  new SimpleDateFormat(formats, Locale.CHINA).format(token2.decodeIdentifier().getIssueDate()));
                        System.out.println("maxdate:" +  new SimpleDateFormat(formats, Locale.CHINA).format(token2.decodeIdentifier().getMaxDate()));
                        System.out.println("seqNumber:" + token2.decodeIdentifier().getSequenceNumber());
                        System.out.println("masterkeyId:" +  token2.decodeIdentifier().getMasterKeyId());
                        System.out.println("trackingId:" +  token2.decodeIdentifier().getTrackingId());
                    }
                }

                System.out.println("node_label_expression: " + appState.getApplicationSubmissionContext().getNodeLabelExpression());
                System.out.println("priority: " + appState.getApplicationSubmissionContext().getPriority());
                System.out.println("resource: " + appState.getApplicationSubmissionContext().getResource());
                System.out.println("appId: " + appState.getApplicationSubmissionContext().getApplicationId());
                System.out.println("name: " + appState.getApplicationSubmissionContext().getApplicationName());
                System.out.println("tags: " + appState.getApplicationSubmissionContext().getApplicationTags());
                System.out.println("type: " + appState.getApplicationSubmissionContext().getApplicationType());
                System.out.println("maxAttempts: " + appState.getApplicationSubmissionContext().getMaxAppAttempts());
                System.out.println("queue: " + appState.getApplicationSubmissionContext().getQueue());
                System.out.println("diagnostics: " + appState.getDiagnostics());
                System.out.println("Hello,hn!");
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
