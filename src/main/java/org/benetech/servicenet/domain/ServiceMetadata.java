package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ServiceMetadata.
 */
@Data
@Entity
@Table(name = "service_metadata")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ServiceMetadata extends AbstractEntity implements Serializable, DeepComparable {

    private static final long serialVersionUID = 1L;

    @Column(name = "last_action_date")
    private ZonedDateTime lastActionDate;

    @NotNull
    @Column(name = "updated_by", nullable = false)
    @Size(max = 255, message = "Field value is too long.")
    private String updatedBy = "";

    @NotNull
    @Column(name = "last_action_type", nullable = false)
    @Size(max = 255, message = "Field value is too long.")
    private String lastActionType = "";

    @ManyToOne
    @JsonIgnoreProperties("metadata")
    private Service srvc;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceMetadata serviceMetadata = (ServiceMetadata) o;
        if (serviceMetadata.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serviceMetadata.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServiceMetadata{" +
            "id=" + getId() +
            "}";
    }

    @Override
    public boolean deepEquals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServiceMetadata sm = (ServiceMetadata) o;
        return Objects.equals(lastActionDate, sm.lastActionDate)
            && Objects.equals(updatedBy, sm.updatedBy)
            && Objects.equals(lastActionType, sm.lastActionType);
    }
}
