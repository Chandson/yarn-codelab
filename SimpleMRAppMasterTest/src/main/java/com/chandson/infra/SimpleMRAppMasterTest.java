package com.chandson.infra;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

public class SimpleMRAppMasterTest {
    public static void test() {

        String jobID = "job_20200520_46";
        SimpleMRAppMaster appMaster = new SimpleMRAppMaster("作业测试", jobID, 10);
        YarnConfiguration conf = new YarnConfiguration(new Configuration());

        try {
            appMaster.serviceInit(conf);

            appMaster.serviceStart();
        } catch (Exception e) {
            e.printStackTrace();
        }

        appMaster.getDispatcher().getEventHandler().handle(new JobEvent(JobEventType.JOB_INIT, jobID));
        appMaster.getDispatcher().getEventHandler().handle(new JobEvent(JobEventType.JOB_KILL, jobID));
    }

    public static void main(String args[]) {
        test();
    }
}
