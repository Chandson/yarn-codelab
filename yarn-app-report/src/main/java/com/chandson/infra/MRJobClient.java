package  com.chandson.infra;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobID;

import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;

public class MRJobClient {
    public static void main(String[] args){
        if (args.length != 1) {
            System.err.println("Please input jobId!");
            System.exit(1);
        }
        String jobId = args[0];

        Configuration conf = new Configuration();
        try {
            // UserGroupInformation.loginUserFromKeytab("u_linyouquan@MY.COMPANY", "u_linyouquan.keytab");
            System.out.println("Current user: " + UserGroupInformation.getCurrentUser());

            JobClient jobClient = new JobClient(conf);
            System.out.println(jobClient.getJob(JobID.forName(jobId)));
        } catch (IOException e) {
            System.out.println(e.toString());
        }

    }
}
