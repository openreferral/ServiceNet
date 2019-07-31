package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A OpeningHours.
 */
@Entity
@Table(name = "opening_hours")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OpeningHours extends AbstractEntity implements Serializable, DeepComparable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "weekday", nullable = false)
    private Integer weekday;

    @Column(name = "opens_at")
    @Size(max = 255, message = "Field value is too long.")
    private String opensAt;

    @Column(name = "closes_at")
    @Size(max = 255, message = "Field value is too long.")
    private String closesAt;

    @ManyToOne
    @JsonIgnoreProperties("openingHours")
    private RegularSchedule regularSchedule;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

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

    @Override
    public boolean deepEquals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OpeningHours oh = (OpeningHours) o;
        return Objects.equals(weekday, oh.weekday) &&
            Objects.equals(opensAt, oh.opensAt) &&
            Objects.equals(closesAt, oh.closesAt);
    }
}
