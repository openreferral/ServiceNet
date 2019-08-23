package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.Objects;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * A MatchSimilarity.
 */
@Entity
@Table(name = "match_similarity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MatchSimilarity extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "similarity")
    private BigDecimal similarity;

    @Column(name = "resource_class")
    private String resourceClass;

    @Column(name = "field_name")
    private String fieldName;

    @ManyToOne
    @JsonIgnoreProperties("matchSimilarities")
    private OrganizationMatch organizationMatch;

    public BigDecimal getSimilarity() {
        return similarity;
    }

    public MatchSimilarity similarity(BigDecimal similarity) {
        this.similarity = similarity;
        return this;
    }

    public void setSimilarity(BigDecimal similarity) {
        this.similarity = similarity;
    }

    public String getResourceClass() {
        return resourceClass;
    }

    public MatchSimilarity resourceClass(String resourceClass) {
        this.resourceClass = resourceClass;
        return this;
    }

    public void setResourceClass(String resourceClass) {
        this.resourceClass = resourceClass;
    }

    public String getFieldName() {
        return fieldName;
    }

    public MatchSimilarity fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public OrganizationMatch getOrganizationMatch() {
        return organizationMatch;
    }

    public MatchSimilarity organizationMatch(OrganizationMatch organizationMatch) {
        this.organizationMatch = organizationMatch;
        return this;
    }

    public void setOrganizationMatch(OrganizationMatch organizationMatch) {
        this.organizationMatch = organizationMatch;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MatchSimilarity)) {
            return false;
        }
        MatchSimilarity matchSimilarity = (MatchSimilarity) o;
        if (matchSimilarity.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), matchSimilarity.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MatchSimilarity{" +
            "id=" + getId() +
            ", similarity=" + getSimilarity() +
            ", resourceClass='" + getResourceClass() + "'" +
            ", fieldName='" + getFieldName() + "'" +
            "}";
    }
}
