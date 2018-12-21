package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.scheduler.ExampleJob;
import org.benetech.servicenet.service.SchedulerService;
import org.benetech.servicenet.service.dto.JobDTO;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SchedulerServiceImpl implements SchedulerService {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private ExampleJob exampleJob;

    @Override
    public List<JobDTO> getAllJobsDetails() throws SchedulerException {
        return getAllTriggers().stream().map(this::mapToJobDTO).collect(Collectors.toList());
    }

    @Override
    public void triggerJob(String name) throws SchedulerException {
        getAllTriggers().stream().filter(t -> t.getJobKey().getName().equals(name)).forEach(t -> rescheduleJob());
    }

    private void rescheduleJob() {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.rescheduleJob(exampleJob.getTrigger().getKey(), exampleJob.getTrigger());
        } catch (SchedulerException e) {
            throw new IllegalStateException("Cannot reschedule job " + exampleJob.getTrigger().getJobKey().getName());
        }
    }

    private List<Trigger> getAllTriggers() throws SchedulerException {
        List<Trigger> result = new ArrayList<>();
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                result.addAll(scheduler.getTriggersOfJob(jobKey));
            }
        }
        return result;
    }

    private JobDTO mapToJobDTO(Trigger trigger) {
        return new JobDTO(trigger.getJobKey().getName(), trigger.getDescription(),
            trigger.getNextFireTime(), trigger.getPreviousFireTime());
    }
}
