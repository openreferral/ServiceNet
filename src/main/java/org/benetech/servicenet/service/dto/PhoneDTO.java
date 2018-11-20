package org.benetech.servicenet.service.dto;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Phone entity.
 */
public class PhoneDTO implements Serializable {

    private Long id;

    @NotNull
    private String number;

    private Integer extension;

    private String type;

    private String language;

    @Lob
    private String description;

    private Long locationId;

    private String locationName;

    private Long srvcId;

    private String srvcName;

    private Long organizationId;

    private String organizationName;

    private Long contactId;

    private String contactName;

    private Long serviceAtLocationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getExtension() {
        return extension;
    }

    public void setExtension(Integer extension) {
        this.extension = extension;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Long getServiceAtLocationId() {
        return serviceAtLocationId;
    }

    public void setServiceAtLocationId(Long serviceAtLocationId) {
        this.serviceAtLocationId = serviceAtLocationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PhoneDTO phoneDTO = (PhoneDTO) o;
        if (phoneDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), phoneDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PhoneDTO{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", extension=" + getExtension() +
            ", type='" + getType() + "'" +
            ", language='" + getLanguage() + "'" +
            ", description='" + getDescription() + "'" +
            ", location=" + getLocationId() +
            ", location='" + getLocationName() + "'" +
            ", srvc=" + getSrvcId() +
            ", srvc='" + getSrvcName() + "'" +
            ", organization=" + getOrganizationId() +
            ", organization='" + getOrganizationName() + "'" +
            ", contact=" + getContactId() +
            ", contact='" + getContactName() + "'" +
            ", serviceAtLocation=" + getServiceAtLocationId() +
            "}";
    }
}
