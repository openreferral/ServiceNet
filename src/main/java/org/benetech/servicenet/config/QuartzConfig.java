package org.benetech.servicenet.config;

import static io.github.jhipster.config.JHipsterConstants.SPRING_PROFILE_DEVELOPMENT;
import static io.github.jhipster.config.JHipsterConstants.SPRING_PROFILE_PRODUCTION;

import java.util.Properties;
import org.benetech.servicenet.scheduler.AutowiringBeanJobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private QuartzProperties quartzProperties;

    @Bean
    @Profile({ SPRING_PROFILE_PRODUCTION, SPRING_PROFILE_DEVELOPMENT} )
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        AutowiringBeanJobFactory jobFactory = new AutowiringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        schedulerFactoryBean.setJobFactory(jobFactory);
        Properties quartzApplicationProperties = new Properties();
        quartzApplicationProperties.putAll(quartzProperties.getProperties());

        schedulerFactoryBean.setQuartzProperties(quartzApplicationProperties);

        return schedulerFactoryBean;
    }
}
