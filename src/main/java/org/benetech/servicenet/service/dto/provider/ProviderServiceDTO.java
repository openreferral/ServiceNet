package org.benetech.servicenet.service.dto.provider;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Lob;
import lombok.Data;
import org.benetech.servicenet.service.dto.PhoneDTO;

@Data
public class ProviderServiceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private String name;

    private String type;

    private List<PhoneDTO> phones;

    private List<String> taxonomyIds;

    private String description;

    private String applicationProcess;

    private String eligibilityCriteria;

    private List<Integer> locationIndexes;

    private Set<ProviderRequiredDocumentDTO> docs;

    @Lob
    private String fees;

    private Boolean medicareAccepted;

    private Boolean medicaidAccepted;

    private Boolean uninsuredAccepted;

    private String insuranceLabel;

    @Lob
    private String safeForUndocumented;
}
