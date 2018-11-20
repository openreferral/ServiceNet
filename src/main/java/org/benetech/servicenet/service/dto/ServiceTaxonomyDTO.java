package org.benetech.servicenet.service.dto;

import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ServiceTaxonomy entity.
 */
public class ServiceTaxonomyDTO implements Serializable {

    private Long id;

    @Lob
    private String taxonomyDetails;

    private Long srvcId;

    private String srvcName;

    private Long taxonomyId;

    private String taxonomyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaxonomyDetails() {
        return taxonomyDetails;
    }

    public void setTaxonomyDetails(String taxonomyDetails) {
        this.taxonomyDetails = taxonomyDetails;
    }

    public Long getSrvcId() {
        return srvcId;
    }

    public void setSrvcId(Long serviceId) {
        this.srvcId = serviceId;
    }

    public String getSrvcName() {
        return srvcName;
    }

    public void setSrvcName(String serviceName) {
        this.srvcName = serviceName;
    }

    public Long getTaxonomyId() {
        return taxonomyId;
    }

    public void setTaxonomyId(Long taxonomyId) {
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
