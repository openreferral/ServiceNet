package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.TaxonomyGroup} entity.
 */
public class TaxonomyGroupDTO implements Serializable {

    private UUID id;

    private Set<TaxonomyDTO> taxonomies = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Set<TaxonomyDTO> getTaxonomies() {
        return taxonomies;
    }

    public void setTaxonomies(Set<TaxonomyDTO> taxonomies) {
        this.taxonomies = taxonomies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TaxonomyGroupDTO taxonomyGroupDTO = (TaxonomyGroupDTO) o;
        if (taxonomyGroupDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taxonomyGroupDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TaxonomyGroupDTO{" +
            "id=" + getId() +
            "}";
    }
}
