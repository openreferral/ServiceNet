package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.SchedulerService;
import org.benetech.servicenet.service.dto.JobDTO;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
public class JobsResource {

    @Autowired
    private SchedulerService schedulerService;

    @GetMapping("/jobs")
    @Timed
    public List<JobDTO> getAllJobs() throws SchedulerException {
        return schedulerService.getAllJobsDetails();
    }

    @PostMapping("/jobs")
    @Timed
    public ResponseEntity triggerJob(@Valid @RequestBody JobDTO jobDTO) throws SchedulerException {
        try {
            schedulerService.triggerJob(jobDTO.getName());
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/jobs/pause")
    @Timed
    public ResponseEntity pauseJob(@Valid @RequestBody JobDTO jobDTO) throws SchedulerException {
        try {
            schedulerService.pauseJob(jobDTO.getName());
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
