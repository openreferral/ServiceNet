package org.benetech.servicenet.config;

import org.benetech.servicenet.scheduler.AutowiringBeanJobFactory;
import org.benetech.servicenet.scheduler.BaseJob;
import org.benetech.servicenet.scheduler.EdenDataUpdateJob;
import org.benetech.servicenet.scheduler.UWBADataUpdateJob;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class QuartzConfig {

    @Autowired
    private EdenDataUpdateJob edenDataUpdateJob;

    @Autowired
    private UWBADataUpdateJob uwbaDataUpdateJob;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();

        AutowiringBeanJobFactory jobFactory = new AutowiringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        scheduler.setJobFactory(jobFactory);

        List<BaseJob> jobs = new ArrayList<>();
        jobs.add(edenDataUpdateJob);
        jobs.add(uwbaDataUpdateJob);

        scheduler.setTriggers(mapToTriggers(jobs));
        scheduler.setJobDetails(mapToJobDetails(jobs));
        return scheduler;
    }

    private JobDetail[] mapToJobDetails(List<BaseJob> jobs) {
        JobDetail[] array = new JobDetail[jobs.size()];
        return jobs.stream().map(BaseJob::getJobDetail).collect(Collectors.toList()).toArray(array);
    }

    private Trigger[] mapToTriggers(List<BaseJob> jobs) {
        Trigger[] array = new Trigger[jobs.size()];
        return jobs.stream().map(BaseJob::getTrigger).collect(Collectors.toList()).toArray(array);
    }
}
