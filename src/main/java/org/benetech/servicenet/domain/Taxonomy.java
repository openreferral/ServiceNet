package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A Taxonomy.
 */
@Entity
@Data
@Table(name = "taxonomy")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Taxonomy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "vocabulary")
    private String vocabulary;

    @Column(name = "external_db_id")
    private String externalDbId;

    @Column(name = "provider_name")
    private String providerName;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Taxonomy parent;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
}
