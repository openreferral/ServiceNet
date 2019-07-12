package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.MatchSimilarity} entity.
 */
public class MatchSimilarityDTO implements Serializable {

    private UUID id;

    private Float similarity;

    private String resourceClass;

    private String fieldName;

    private UUID organizationMatchId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Float similarity) {
        this.similarity = similarity;
    }

    public String getResourceClass() {
        return resourceClass;
    }

    public void setResourceClass(String resourceClass) {
        this.resourceClass = resourceClass;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public UUID getOrganizationMatchId() {
        return organizationMatchId;
    }

    public void setOrganizationMatchId(UUID organizationMatchId) {
        this.organizationMatchId = organizationMatchId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MatchSimilarityDTO matchSimilarityDTO = (MatchSimilarityDTO) o;
        if (matchSimilarityDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), matchSimilarityDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MatchSimilarityDTO{" +
            "id=" + getId() +
            ", similarity=" + getSimilarity() +
            ", resourceClass='" + getResourceClass() + "'" +
            ", fieldName='" + getFieldName() + "'" +
            ", organizationMatch=" + getOrganizationMatchId() +
            "}";
    }
}
