package org.benetech.servicenet.config;

import static io.github.jhipster.config.JHipsterConstants.SPRING_PROFILE_DEVELOPMENT;
import static io.github.jhipster.config.JHipsterConstants.SPRING_PROFILE_PRODUCTION;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.benetech.servicenet.scheduler.AutowiringBeanJobFactory;
import org.benetech.servicenet.scheduler.BaseJob;
import org.benetech.servicenet.scheduler.EdenDataUpdateJob;
import org.benetech.servicenet.scheduler.EdenTaxonomyUpdateJob;
import org.benetech.servicenet.scheduler.SMCConnectTaxonomyUpdateJob;
import org.benetech.servicenet.scheduler.ShelterTechDataUpdateJob;
import org.benetech.servicenet.scheduler.UWBADataUpdateJob;
import org.benetech.servicenet.scheduler.UWBATaxonomyUpdateJob;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

    @Autowired
    private EdenDataUpdateJob edenDataUpdateJob;

    @Autowired
    private UWBADataUpdateJob uwbaDataUpdateJob;

    @Autowired
    private ShelterTechDataUpdateJob shelterTechDataUpdateJob;

    @Autowired
    private SMCConnectTaxonomyUpdateJob smcConnectTaxonomyUpdateJob;

    @Autowired
    private EdenTaxonomyUpdateJob edenTaxonomyUpdateJob;

    @Autowired
    private UWBATaxonomyUpdateJob uwbaTaxonomyUpdateJob;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @Profile({ SPRING_PROFILE_PRODUCTION, SPRING_PROFILE_DEVELOPMENT} )
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();

        AutowiringBeanJobFactory jobFactory = new AutowiringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        scheduler.setJobFactory(jobFactory);

        List<BaseJob> jobs = new ArrayList<>();
        jobs.add(edenDataUpdateJob);
        jobs.add(uwbaDataUpdateJob);
        jobs.add(shelterTechDataUpdateJob);
        jobs.add(smcConnectTaxonomyUpdateJob);
        jobs.add(edenTaxonomyUpdateJob);
        jobs.add(uwbaTaxonomyUpdateJob);

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
        return jobs.stream().map(BaseJob::getInitTrigger).collect(Collectors.toList()).toArray(array);
    }
}
