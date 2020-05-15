package org.benetech.servicenet.service.dto.provider;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import org.benetech.servicenet.service.dto.ServiceTaxonomyDTO;

@Data
public class SimpleServiceDTO implements Serializable {

    private UUID id;

    private String name;

    private String type;

    private List<String> taxonomyIds;

    private String description;

    private String applicationProcess;

    private String eligibilityCriteria;

    private List<String> locationIndexes;
}
