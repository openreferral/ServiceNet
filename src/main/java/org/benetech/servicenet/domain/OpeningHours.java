package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A OpeningHours.
 */
@Entity
@Table(name = "opening_hours")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OpeningHours implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @NotNull
    @Column(name = "weekday", nullable = false)
    private Integer weekday;

    @Column(name = "opens_at")
    private String opensAt;

    @Column(name = "closes_at")
    private String closesAt;

    @ManyToOne
    @JsonIgnoreProperties("openingHours")
    private RegularSchedule regularSchedule;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getWeekday() {
        return weekday;
    }

    public OpeningHours weekday(Integer weekday) {
        this.weekday = weekday;
        return this;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public String getOpensAt() {
        return opensAt;
    }

    public OpeningHours opensAt(String opensAt) {
        this.opensAt = opensAt;
        return this;
    }

    public void setOpensAt(String opensAt) {
        this.opensAt = opensAt;
    }

    public String getClosesAt() {
        return closesAt;
    }

    public OpeningHours closesAt(String closesAt) {
        this.closesAt = closesAt;
        return this;
    }

    public void setClosesAt(String closesAt) {
        this.closesAt = closesAt;
    }

    public RegularSchedule getRegularSchedule() {
        return regularSchedule;
    }

    public OpeningHours regularSchedule(RegularSchedule regularSchedule) {
        this.regularSchedule = regularSchedule;
        return this;
    }

    public void setRegularSchedule(RegularSchedule regularSchedule) {
        this.regularSchedule = regularSchedule;
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
        OpeningHours openingHours = (OpeningHours) o;
        if (openingHours.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), openingHours.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OpeningHours{" +
            "id=" + getId() +
            ", weekday=" + getWeekday() +
            ", opensAt='" + getOpensAt() + "'" +
            ", closesAt='" + getClosesAt() + "'" +
            "}";
    }
}
