package  com.chandson.infra;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.security.token.Token;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;

import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.yarn.security.client.ClientToAMTokenIdentifier;
import org.apache.hadoop.yarn.util.ConverterUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.EnumSet;
import java.util.List;

public class AppReport {
    public static void main(String[] args){
        Configuration conf = new YarnConfiguration();
        YarnClient yarnClient = YarnClient.createYarnClient();
        yarnClient.init(conf);
        yarnClient.start();

        try {
            // UserGroupInformation.loginUserFromKeytab("u_linyouquan@MY.COMPANY", "u_linyouquan.keytab");
            System.out.println("Current user: " + UserGroupInformation.getCurrentUser());

            // ApplicationId appId = ConverterUtils.toApplicationId("application_xxxx_xxxx");
            List<ApplicationReport> applications = yarnClient.getApplications(EnumSet.of(YarnApplicationState.RUNNING));
            for (ApplicationReport application: applications) {
                System.out.println("appId: " + application.getApplicationId() + ", name: " + application.getName() +
                        ", queue: " + application.getQueue());

                // debug ClientToAMTokenIdentifier
                InetSocketAddress serviceAddr = null;
                serviceAddr = NetUtils.createSocketAddrForHost(
                        application.getHost(), application.getRpcPort());
                Token<ClientToAMTokenIdentifier> token =
                        ConverterUtils.convertFromYarn(application.getClientToAMToken(), serviceAddr);
                System.out.println("attemptId: " + token.decodeIdentifier().getApplicationAttemptID());
                System.out.println("client: " + token.decodeIdentifier().getClientName());
                System.out.println("user: " + token.decodeIdentifier().getUser());
                System.out.println("service: " + token.getService());
            }
        } catch (YarnException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        yarnClient.stop();
    }
}
