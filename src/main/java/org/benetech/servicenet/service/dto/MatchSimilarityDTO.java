package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Data;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.MatchSimilarity} entity.
 */
@Data
public class MatchSimilarityDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private BigDecimal similarity;

    private String resourceClass;

    private String fieldName;

    private UUID organizationMatchId;

    private BigDecimal weight;

    List<LocationMatchDto> matchesToRemove;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getSimilarity() {
        return similarity;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setSimilarity(BigDecimal similarity) {
        this.similarity = similarity;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
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
