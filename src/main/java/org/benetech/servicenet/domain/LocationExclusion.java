package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * A LocationExclusion.
 */
@Entity
@Table(name = "location_exclusion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LocationExclusion extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "region")
    private String region;

    @Column(name = "city")
    private String city;

    @ManyToOne
    @JsonIgnoreProperties("locationExclusions")
    private ExclusionsConfig config;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getRegion() {
        return region;
    }

    public LocationExclusion region(String region) {
        this.region = region;
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public LocationExclusion city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ExclusionsConfig getConfig() {
        return config;
    }

    public LocationExclusion config(ExclusionsConfig exclusionsConfig) {
        this.config = exclusionsConfig;
        return this;
    }

    public void setConfig(ExclusionsConfig exclusionsConfig) {
        this.config = exclusionsConfig;
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
        LocationExclusion locationExclusion = (LocationExclusion) o;
        if (locationExclusion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), locationExclusion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LocationExclusion{" +
            "id=" + getId() +
            ", region='" + getRegion() + "'" +
            ", city='" + getCity() + "'" +
            "}";
    }
}
