package org.benetech.servicenet.scheduler;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.scheduling.quartz.QuartzJobBean;

public abstract class BaseJob extends QuartzJobBean {

    private static final String TRIGGER = "Trigger";

    public abstract String getDescription();

    public abstract String getFullName();

    public abstract int getIntervalInSeconds();

    public JobDetail getJobDetail() {
        return JobBuilder.newJob(getClass())
            .withIdentity(getFullName())
            .storeDurably()
            .build();
    }

    public Trigger getTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder
            .simpleSchedule()
            .withIntervalInSeconds(getIntervalInSeconds())
            .repeatForever();

        return TriggerBuilder
            .newTrigger()
            .withDescription(getDescription())
            .forJob(getJobDetail())
            .withIdentity(getFullName() + TRIGGER)
            .withSchedule(scheduleBuilder)
            .build();
    }
}
