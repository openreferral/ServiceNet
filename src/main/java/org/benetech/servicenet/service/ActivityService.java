package org.benetech.servicenet.service;

import org.benetech.servicenet.service.dto.ActivityDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Service Interface for managing Activity.
 */
public interface ActivityService {

    List<ActivityDTO> getAllOrganizationActivities(UUID systemAccountId);

    Page<ActivityDTO> getAllOrganizationActivities(Pageable pageable, UUID systemAccountId);

}
