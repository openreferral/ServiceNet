package org.benetech.servicenet.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the RequiredDocument entity.
 */
public class RequiredDocumentDTO implements Serializable {

    private Long id;

    @NotNull
    private String document;

    private Long srvcId;

    private String srvcName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
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

        RequiredDocumentDTO requiredDocumentDTO = (RequiredDocumentDTO) o;
        if (requiredDocumentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), requiredDocumentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RequiredDocumentDTO{" +
            "id=" + getId() +
            ", document='" + getDocument() + "'" +
            ", srvc=" + getSrvcId() +
            ", srvc='" + getSrvcName() + "'" +
            "}";
    }
}
