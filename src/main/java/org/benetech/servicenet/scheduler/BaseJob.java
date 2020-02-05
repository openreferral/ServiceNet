package org.benetech.servicenet.scheduler;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Calendar;
import java.util.Date;

public abstract class BaseJob extends QuartzJobBean {

    private static final String TRIGGER = "Trigger";

    public abstract String getDescription();

    public abstract String getFullName();

    public abstract int getIntervalInSeconds();

    public abstract Date getStartTime();

    public JobDetail getJobDetail() {
        return JobBuilder.newJob(getClass())
            .withIdentity(getFullName())
            .storeDurably()
            .build();
    }

    public Trigger getTrigger() {
        return TriggerBuilder
            .newTrigger()
            .withDescription(getDescription())
            .forJob(getJobDetail())
            .withIdentity(getFullName() + TRIGGER)
            .withSchedule(getSchedule())
            .startNow()
            .build();
    }

    public Trigger getInitTrigger() {
        return TriggerBuilder
            .newTrigger()
            .withDescription(getDescription())
            .forJob(getJobDetail())
            .withIdentity(getFullName() + TRIGGER)
            .withSchedule(getSchedule())
            .startAt(getStartTime())
            .build();
    }

    protected SimpleScheduleBuilder getSchedule() {
        return SimpleScheduleBuilder
            .simpleSchedule()
            .withIntervalInSeconds(getIntervalInSeconds())
            .repeatForever();
    }

    protected Date getOffsetDate(int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

}
