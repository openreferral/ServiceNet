package org.benetech.servicenet.service.dto.provider;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class SimpleServiceDTO implements Serializable {

    private UUID id;

    private String name;

    private List<String> type;

    private String description;

    private String applicationProcess;

    private String eligibilityCriteria;

    private List<String> locationIndexes;
}
