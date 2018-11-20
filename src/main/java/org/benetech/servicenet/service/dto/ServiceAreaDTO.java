package org.benetech.servicenet.service.dto;

import javax.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ServiceArea entity.
 */
public class ServiceAreaDTO implements Serializable {

    private Long id;

    @Lob
    private String description;

    private Long srvcId;

    private String srvcName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServiceAreaDTO serviceAreaDTO = (ServiceAreaDTO) o;
        if (serviceAreaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceAreaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiceAreaDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", srvc=" + getSrvcId() +
            ", srvc='" + getSrvcName() + "'" +
            "}";
    }
}
