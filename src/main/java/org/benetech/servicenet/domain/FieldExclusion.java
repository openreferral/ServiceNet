package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FieldExclusion.
 */
@Data
@Entity
@Table(name = "field_exclusion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FieldExclusion extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Lob
    @Column(name = "fields")
    private String fields;

    @Column(name = "entity")
    private String entity;

    @ManyToOne
    @JsonIgnoreProperties("exclusions")
    private ExclusionsConfig config;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public FieldExclusion fields(String fields) {
        this.fields = fields;
        return this;
    }

    public FieldExclusion entity(String entity) {
        this.entity = entity;
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
        FieldExclusion fieldExclusion = (FieldExclusion) o;
        if (fieldExclusion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fieldExclusion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
