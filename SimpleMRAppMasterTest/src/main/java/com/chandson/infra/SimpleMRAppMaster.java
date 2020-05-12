package com.chandson.infra;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.service.CompositeService;
import org.apache.hadoop.service.Service;
import org.apache.hadoop.yarn.event.AsyncDispatcher;
import org.apache.hadoop.yarn.event.Dispatcher;
import org.apache.hadoop.yarn.event.EventHandler;

public class SimpleMRAppMaster extends CompositeService {
    private Dispatcher dispatcher;   //中央异步调度器
    private String jobID;
    private int taskNumber;  //作业中包含的任务数
    private String[] taskIDS; //该作业中包含的所有任务

    public SimpleMRAppMaster(String name, String jobID, int taskNumber) {
        super(name);
        this.jobID = jobID;
        this.taskNumber = taskNumber;
        this.taskIDS = new String[taskNumber];
        for(int i=0; i<taskNumber; i++){
            this.taskIDS[i] = new String(jobID + "_task_" + i);
        }
    }

    @Override
    protected void serviceInit(Configuration conf) throws Exception {

        dispatcher = new AsyncDispatcher();

        dispatcher.register(JobEventType.class, new JobEventHandller());
        dispatcher.register(TaskEventType.class, new TaskEventHandller());
        addService((Service)dispatcher);

        super.serviceInit(conf);
    }

    @Override
    protected void serviceStart() throws Exception {
        super.serviceStart();
    }

    public Dispatcher getDispatcher(){
        return dispatcher;
    }

    private class JobEventHandller implements EventHandler<JobEvent> {
        public void handle(JobEvent event) {
            if(event.getType() == JobEventType.JOB_KILL){
                System.out.println("收到杀死作业事件,要杀掉作业" + event.getJobID() + "下的所有任务");
                for(int i=0; i<=taskNumber; i++){
                    dispatcher.getEventHandler().handle(new TaskEvent(TaskEventType.T_KILL, taskIDS[i]));
                }
            } else if(event.getType() == JobEventType.JOB_INIT){
                System.out.println("收到启动作业事件,要启动作业" + event.getJobID() + "下的所有任务");
                for(int i=0; i<=taskNumber; i++){
                    dispatcher.getEventHandler().handle(new TaskEvent(TaskEventType.T_SCHEDULE, taskIDS[i]));
                }
            }
        }
    }

    private class TaskEventHandller implements EventHandler<TaskEvent>{
        public void handle(TaskEvent event) {
            if(event.getType() == TaskEventType.T_KILL){
                System.out.println("收到杀死任务命令，开始杀死任务" + event.getTaskID());
            }else if(event.getType() == TaskEventType.T_SCHEDULE){
                System.out.println("收到启动任务命令，开始启动任务" + event.getTaskID());
            }
        }
    }
}
