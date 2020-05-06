package org.benetech.servicenet.service.dto.provider;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class SimpleServiceDTO implements Serializable {

    private String name;

    private List<String> type;

    private String description;

    private String applicationProcess;

    private String eligibilityCriteria;

    private List<String> locationIndexes;
}
