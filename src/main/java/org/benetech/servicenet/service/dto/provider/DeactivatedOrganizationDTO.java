package org.benetech.servicenet.service.dto.provider;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class DeactivatedOrganizationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private String name;

    private ZonedDateTime updatedAt;

    private ZonedDateTime deactivatedAt;
}
