package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

/**
 * A DTO for the ServiceMatch entity.
 */
@Data
public class ServiceMatchDto implements Serializable {

    private UUID id;

    private UUID service;

    private UUID matchingService;
}
