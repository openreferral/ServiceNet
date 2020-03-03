package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ServiceMatch.
 */
@Entity
@Data
@Table(name = "service_match")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ServiceMatch extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnoreProperties("")
    @ManyToOne
    private Service service;

    @JsonIgnoreProperties("")
    @ManyToOne
    private Service matchingService;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceMatch srvc = (ServiceMatch) o;
        if (srvc.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(this.getId(), srvc.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
