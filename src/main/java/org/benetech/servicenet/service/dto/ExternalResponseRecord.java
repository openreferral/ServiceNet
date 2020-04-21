package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExternalResponseRecord implements Serializable {

    private UUID serviceNetId;

    private String externalDbId;

    private String organizationName;

    private Double similarity;
}
