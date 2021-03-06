package com.chandson.infra;

import org.apache.hadoop.yarn.event.AbstractEvent;

public class JobEvent extends AbstractEvent<JobEventType> {
    private String jobID;

    public JobEvent(JobEventType type, String jobID) {
        super(type);
        this.jobID = jobID;
    }

    public String getJobID() {
        return jobID;
    }
}
