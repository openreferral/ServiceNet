package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AccessibilityForDisabilities.
 */
@Entity
@Data
@Table(name = "accessibility_for_disabilities")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AccessibilityForDisabilities extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @NotNull
    @Column(name = "accessibility", nullable = false, columnDefinition = "clob")
    private String accessibility;

    @Column(name = "details")
    private String details;

    @ManyToOne
    @JsonIgnoreProperties("accessibilities")
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public AccessibilityForDisabilities accessibility(String accessibility) {
        this.accessibility = accessibility;
        return this;
    }

    public AccessibilityForDisabilities details(String details) {
        this.details = details;
        return this;
    }

    public AccessibilityForDisabilities location(Location location) {
        this.location = location;
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
        AccessibilityForDisabilities accessibilityForDisabilities = (AccessibilityForDisabilities) o;
        if (accessibilityForDisabilities.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accessibilityForDisabilities.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccessibilityForDisabilities{" +
            "id=" + getId() +
            ", accessibility='" + getAccessibility() + "'" +
            ", details='" + getDetails() + "'" +
            "}";
    }
}
