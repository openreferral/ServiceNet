package org.benetech.servicenet.domain;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A GeocodingResult.
 */
@Data
@Entity
@Table(name = "geocoding_result")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GeocodingResult extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Lob
    @NotNull
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "address", columnDefinition = "clob", nullable = false)
    private String address;

    @NotNull
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public GeocodingResult address(String address) {
        this.address = address;
        return this;
    }

    public GeocodingResult latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public GeocodingResult longitude(Double longitude) {
        this.longitude = longitude;
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
        GeocodingResult geocodingResult = (GeocodingResult) o;
        if (geocodingResult.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), geocodingResult.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GeocodingResult{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            "}";
    }
}
