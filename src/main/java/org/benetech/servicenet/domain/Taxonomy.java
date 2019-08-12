package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Taxonomy.
 */
@Entity
@Data
@Table(name = "taxonomy", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"external_db_id", "provider_name"})
})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Taxonomy extends AbstractEntity implements Serializable, DeepComparable {

    private static final long serialVersionUID = 1L;

    @Column(name = "name")
    @Size(max = 255, message = "Field value is too long.")
    private String name;

    @Column(name = "taxonomy_id")
    @Size(max = 255, message = "Field value is too long.")
    private String taxonomyId;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "details", columnDefinition = "clob")
    private String details;

    @Column(name = "vocabulary")
    @Size(max = 255, message = "Field value is too long.")
    private String vocabulary;

    @Column(name = "external_db_id")
    @Size(max = 255, message = "Field value is too long.")
    private String externalDbId;

    @Column(name = "provider_name")
    @Size(max = 255, message = "Field value is too long.")
    private String providerName;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Taxonomy parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Taxonomy name(String name) {
        this.name = name;
        return this;
    }

    public String getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(String vocabulary) {
        this.vocabulary = vocabulary;
    }

    public Taxonomy vocabulary(String vocabulary) {
        this.vocabulary = vocabulary;
        return this;
    }

    public Taxonomy getParent() {
        return parent;
    }

    public void setParent(Taxonomy taxonomy) {
        this.parent = taxonomy;
    }

    public Taxonomy parent(Taxonomy taxonomy) {
        this.parent = taxonomy;
        return this;
    }

    public Taxonomy externalDbId(String externalDbId) {
        this.externalDbId = externalDbId;
        return this;
    }

    public Taxonomy providerName(String providerName) {
        this.providerName = providerName;
        return this;
    }

    public Taxonomy taxonomyId(String taxonomyId) {
        this.taxonomyId = taxonomyId;
        return this;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Taxonomy taxonomy = (Taxonomy) o;
        if (taxonomy.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), taxonomy.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Taxonomy{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", vocabulary='" + getVocabulary() + "'" +
            "}";
    }

    @SuppressWarnings("checkstyle:booleanExpressionComplexity")
    @Override
    public boolean deepEquals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Taxonomy taxonomy = (Taxonomy) o;
        return Objects.equals(name, taxonomy.name) &&
            Objects.equals(taxonomyId, taxonomy.taxonomyId) &&
            Objects.equals(details, taxonomy.details) &&
            Objects.equals(vocabulary, taxonomy.vocabulary) &&
            Objects.equals(externalDbId, taxonomy.externalDbId) &&
            Objects.equals(providerName, taxonomy.providerName);
    }
}
