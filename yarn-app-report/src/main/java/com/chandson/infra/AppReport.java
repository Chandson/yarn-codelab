package  com.chandson.infra;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;

import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.yarn.util.ConverterUtils;

import java.io.IOException;
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
            }
        } catch (YarnException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        yarnClient.stop();
    }
}
