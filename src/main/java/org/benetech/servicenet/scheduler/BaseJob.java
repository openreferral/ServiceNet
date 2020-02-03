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

    public JobDetail getJobDetail() {
        return JobBuilder.newJob(getClass())
            .withIdentity(getFullName())
            .storeDurably()
            .build();
    }

    public Trigger getTrigger() {
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder
            .newTrigger()
            .withDescription(getDescription())
            .forJob(getJobDetail())
            .withIdentity(getFullName() + TRIGGER);
        if (getIntervalInSeconds() > 0) {
            return triggerBuilder
                .withSchedule(getSchedule())
                .startNow()
                .build();
        } else {
            return triggerBuilder
                .withSchedule(getMaxIntervalSchedule())
                .startNow()
                .build();
        }
    }

    public Trigger getInitTrigger() {
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder
            .newTrigger()
            .withDescription(getDescription())
            .forJob(getJobDetail())
            .withIdentity(getFullName() + TRIGGER);
        if (getIntervalInSeconds() > 0) {
            return triggerBuilder
                .withSchedule(getSchedule())
                .startAt(startAnHourFromNow())
                .build();
        } else {
            return triggerBuilder
                .withSchedule(getMaxIntervalSchedule())
                .startNow()
                .build();
        }
    }

    protected SimpleScheduleBuilder getSchedule() {
        return SimpleScheduleBuilder
            .simpleSchedule()
            .withIntervalInSeconds(getIntervalInSeconds())
            .repeatForever();
    }

    protected SimpleScheduleBuilder getMaxIntervalSchedule() {
        return SimpleScheduleBuilder
            .simpleSchedule()
            .withIntervalInSeconds(Integer.MAX_VALUE)
            .repeatForever();
    }

    private Date startAnHourFromNow() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        return calendar.getTime();
    }

}
