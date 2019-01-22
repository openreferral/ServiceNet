package org.benetech.servicenet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A ConfidentialRecord.
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "confidential_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ConfidentialRecord extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "resource_id")
    private UUID resourceId;

    @Column(name = "fields")
    private String fields;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public ConfidentialRecord fields(String fields) {
        this.fields = fields;
        return this;
    }

    public void setFields(String fields) {
        this.fields = fields;
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
        ConfidentialRecord confidentialRecord = (ConfidentialRecord) o;
        if (confidentialRecord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), confidentialRecord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfidentialRecord{" +
            "id=" + getId() +
            ", resourceId='" + getResourceId() + "'" +
            ", fields='" + getFields() + "'" +
            "}";
    }
}
