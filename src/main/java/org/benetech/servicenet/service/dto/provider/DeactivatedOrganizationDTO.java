package org.benetech.servicenet.service.dto.provider;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class DeactivatedOrganizationDTO implements Serializable {

    private UUID id;

    private String name;

    private ZonedDateTime updatedAt;

    private ZonedDateTime deactivatedAt;
}
