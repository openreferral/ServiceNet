package org.benetech.servicenet.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the Taxonomy entity.
 */
@Data
public class TaxonomyDTO implements Serializable {

    private UUID id;

    private String name;

    private String vocabulary;

    private UUID parentId;

    private String parentName;

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

        TaxonomyDTO taxonomyDTO = (TaxonomyDTO) o;
        if (taxonomyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taxonomyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaxonomyDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", vocabulary='" + getVocabulary() + "'" +
            ", parent=" + getParentId() +
            ", parent='" + getParentName() + "'" +
            "}";
    }
}
