package com.chandson.infra;

import org.apache.hadoop.yarn.event.AbstractEvent;

public class TaskEvent extends AbstractEvent<TaskEventType> {
    private String taskID; //TASkID

    public TaskEvent(TaskEventType type, String taskID) {
        super(type);
        this.taskID = taskID;
    }

    public String getTaskID() {
        return taskID;
    }
}
