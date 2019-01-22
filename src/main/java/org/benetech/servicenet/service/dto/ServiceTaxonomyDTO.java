package org.benetech.servicenet.service.dto;

import lombok.Data;

import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the ServiceTaxonomy entity.
 */
@Data
public class ServiceTaxonomyDTO implements Serializable {

    private UUID id;

    @Lob
    private String taxonomyDetails;

    private UUID srvcId;

    private String srvcName;

    private UUID taxonomyId;

    private String taxonomyName;

    private String externalDbId;

    private String providerName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceTaxonomyDTO serviceTaxonomyDTO = (ServiceTaxonomyDTO) o;
        if (serviceTaxonomyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceTaxonomyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiceTaxonomyDTO{" +
            "id=" + getId() +
            ", taxonomyDetails='" + getTaxonomyDetails() + "'" +
            ", srvc=" + getSrvcId() +
            ", srvc='" + getSrvcName() + "'" +
            ", taxonomy=" + getTaxonomyId() +
            ", taxonomy='" + getTaxonomyName() + "'" +
            "}";
    }
}
