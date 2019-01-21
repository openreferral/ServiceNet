package org.benetech.servicenet.service.impl;

import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.scheduler.BaseJob;
import org.benetech.servicenet.scheduler.EdenDataUpdateJob;
import org.benetech.servicenet.scheduler.UWBADataUpdateJob;
import org.benetech.servicenet.service.DataImportReportService;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SchedulerServiceImpl implements SchedulerService {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private EdenDataUpdateJob edenDataUpdateJob;

    @Autowired
    private UWBADataUpdateJob uwbaDataUpdateJob;

    @Autowired
    private DataImportReportService dataImportReportService;

    @Override
    public List<JobDTO> getAllJobsDetails() throws SchedulerException {
        return getAllTriggers().stream().map(this::mapToJobDTO).collect(Collectors.toList());
    }

    @Override
    public void triggerJob(String name) throws SchedulerException {
        getTriggersByName(name).forEach(this::rescheduleJob);
    }

    @Override
    public void pauseJob(String name) throws SchedulerException {
        getTriggersByName(name).forEach(this::pauseJob);
    }

    private List<Trigger> getTriggersByName(String name) throws SchedulerException {
        return getAllTriggers().stream().filter(t -> t.getJobKey().getName().equals(name)).collect(Collectors.toList());
    }

    private void rescheduleJob(Trigger trigger) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            scheduler.rescheduleJob(trigger.getKey(), getBeanTrigger(trigger.getJobKey().getName()));
        } catch (SchedulerException e) {
            throw new IllegalStateException("Cannot reschedule job " + trigger.getJobKey().getName());
        }
    }

    private void pauseJob(Trigger trigger) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.pauseTrigger(trigger.getKey());
            scheduler.pauseJob(trigger.getJobKey());
        } catch (SchedulerException e) {
            throw new IllegalStateException("Cannot pause job " + trigger.getJobKey().getName());
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
        try {
            Trigger.TriggerState state = schedulerFactoryBean.getScheduler().getTriggerState(trigger.getKey());
            DataImportReport report = dataImportReportService.findLatestByJobName(trigger.getJobKey().getName());
            UUID lastReportId = report != null ? report.getId() : null;
            return new JobDTO(trigger.getJobKey().getName(), trigger.getDescription(),
                trigger.getNextFireTime(), trigger.getPreviousFireTime(), state.name(), lastReportId);
        } catch (SchedulerException e) {
            throw new IllegalStateException("Cannot get details of " + trigger.getJobKey());
        }
    }

    private Trigger getBeanTrigger(String name) {
        List<BaseJob> allBeans = new ArrayList<>();
        allBeans.add(edenDataUpdateJob);
        allBeans.add(uwbaDataUpdateJob);

        return allBeans.stream()
            .filter(b -> b.getFullName().equals(name))
            .map(BaseJob::getTrigger)
            .findFirst().orElseThrow();
    }
}
