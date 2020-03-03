package org.benetech.servicenet.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.scheduler.BaseJob;
import org.benetech.servicenet.scheduler.EdenDataUpdateJob;
import org.benetech.servicenet.scheduler.EdenTaxonomyUpdateJob;
import org.benetech.servicenet.scheduler.GeocodingResultUpdateJob;
import org.benetech.servicenet.scheduler.OrganizationMatchDiscoveryJob;
import org.benetech.servicenet.scheduler.OrganizationMatchUpdateJob;
import org.benetech.servicenet.scheduler.SMCConnectTaxonomyUpdateJob;
import org.benetech.servicenet.scheduler.ShelterTechDataUpdateJob;
import org.benetech.servicenet.scheduler.UWBADataUpdateJob;
import org.benetech.servicenet.scheduler.UWBATaxonomyUpdateJob;
import org.benetech.servicenet.service.DataImportReportService;
import org.benetech.servicenet.service.SchedulerService;
import org.benetech.servicenet.service.dto.JobDTO;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SchedulerServiceImpl implements SchedulerService {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

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
    private OrganizationMatchDiscoveryJob organizationMatchDiscoveryJob;

    @Autowired
    private OrganizationMatchUpdateJob organizationMatchUpdateJob;

    @Autowired
    private DataImportReportService dataImportReportService;

    @Autowired
    private GeocodingResultUpdateJob geocodingResultUpdateJob;

    @PostConstruct
    private void loadJobsAndTriggersOnStartupIfNeeded() {

        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        List<BaseJob> jobs = new ArrayList<>();
        jobs.add(edenDataUpdateJob);
        jobs.add(uwbaDataUpdateJob);
        jobs.add(shelterTechDataUpdateJob);
        jobs.add(smcConnectTaxonomyUpdateJob);
        jobs.add(edenTaxonomyUpdateJob);
        jobs.add(uwbaTaxonomyUpdateJob);
        jobs.add(organizationMatchDiscoveryJob);
        jobs.add(organizationMatchUpdateJob);
        jobs.add(geocodingResultUpdateJob);

        jobs
            .forEach(job -> {
                try {
                    if (scheduler.getJobDetail(job.getJobDetail().getKey()) == null) {
                        scheduler.scheduleJob(job.getJobDetail(), job.getInitTrigger());
                    }
                } catch (SchedulerException e) {
                    log.error(e.getMessage(), e);
                }
            });
    }

    @Override
    public List<JobDTO> getAllJobsDetails() throws SchedulerException {
        return getAllJobs().stream().map(this::mapToJobDTO).collect(Collectors.toList());
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

    private List<JobDetail> getAllJobs() throws SchedulerException {
        List<JobDetail> result = new ArrayList<>();
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                result.add(scheduler.getJobDetail(jobKey));
            }
        }
        return result;
    }

    private JobDTO mapToJobDTO(JobDetail jobDetail) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetail.getKey());
            Trigger trigger = (triggers.size() > 0) ? triggers.get(0) : null;
            DataImportReport report = dataImportReportService.findLatestByJobName(jobDetail.getKey().getName());
            return new JobDTO(jobDetail.getKey().getName(),
                (trigger != null) ? trigger.getDescription() : null,
                (trigger != null) ? trigger.getNextFireTime() : null,
                (trigger != null) ? trigger.getPreviousFireTime() : null,
                (trigger != null) ? schedulerFactoryBean.getScheduler().getTriggerState(trigger.getKey()).name() : null,
                (report != null) ? report.getId() : null);
        } catch (SchedulerException e) {
            throw new IllegalStateException("Cannot get details of " + jobDetail.getKey());
        }
    }

    private Trigger getBeanTrigger(String name) {
        List<BaseJob> allBeans = new ArrayList<>();
        allBeans.add(edenDataUpdateJob);
        allBeans.add(uwbaDataUpdateJob);
        allBeans.add(shelterTechDataUpdateJob);
        allBeans.add(smcConnectTaxonomyUpdateJob);
        allBeans.add(edenTaxonomyUpdateJob);
        allBeans.add(uwbaTaxonomyUpdateJob);
        allBeans.add(organizationMatchDiscoveryJob);
        allBeans.add(organizationMatchUpdateJob);
        allBeans.add(geocodingResultUpdateJob);

        return allBeans.stream()
            .filter(b -> b.getFullName().equals(name))
            .map(BaseJob::getTrigger)
            .findFirst().orElseThrow();
    }
}
