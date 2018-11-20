package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Taxonomy entity.
 */
public class TaxonomyDTO implements Serializable {

    private Long id;

    private String name;

    private String vocabulary;

    private Long parentId;

    private String parentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(String vocabulary) {
        this.vocabulary = vocabulary;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long taxonomyId) {
        this.parentId = taxonomyId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String taxonomyName) {
        this.parentName = taxonomyName;
    }

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
