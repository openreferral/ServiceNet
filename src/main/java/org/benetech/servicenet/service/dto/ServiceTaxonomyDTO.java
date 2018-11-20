package org.benetech.servicenet.service.dto;

import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the ServiceTaxonomy entity.
 */
public class ServiceTaxonomyDTO implements Serializable {

    private UUID id;

    @Lob
    private String taxonomyDetails;

    private UUID srvcId;

    private String srvcName;

    private UUID taxonomyId;

    private String taxonomyName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTaxonomyDetails() {
        return taxonomyDetails;
    }

    public void setTaxonomyDetails(String taxonomyDetails) {
        this.taxonomyDetails = taxonomyDetails;
    }

    public UUID getSrvcId() {
        return srvcId;
    }

    public void setSrvcId(UUID serviceId) {
        this.srvcId = serviceId;
    }

    public String getSrvcName() {
        return srvcName;
    }

    public void setSrvcName(String serviceName) {
        this.srvcName = serviceName;
    }

    public UUID getTaxonomyId() {
        return taxonomyId;
    }

    public void setTaxonomyId(UUID taxonomyId) {
        this.taxonomyId = taxonomyId;
    }

    public String getTaxonomyName() {
        return taxonomyName;
    }

    public void setTaxonomyName(String taxonomyName) {
        this.taxonomyName = taxonomyName;
    }

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
