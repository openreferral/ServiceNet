package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.JobDTO;
import org.quartz.SchedulerException;

import java.util.List;

public interface SchedulerService {

    List<JobDTO> getAllJobsDetails() throws SchedulerException;

    void triggerJob(String name) throws SchedulerException;
}
