package org.benetech.servicenet.service.dto.external;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID serviceNetId;

    private String externalDbId;

    private String organizationName;

    private Double similarity;
}
