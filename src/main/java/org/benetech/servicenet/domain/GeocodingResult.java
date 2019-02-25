package org.benetech.servicenet.domain;

import com.google.maps.model.LatLng;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A GeocodingResult.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "geocoding_result")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GeocodingResult extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "address", nullable = false)
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

    public LatLng getGoogleCoords() {
        return new LatLng(this.latitude, this.longitude);
    }

    public GeocodingResult(@NotNull String address, @NotNull com.google.maps.model.GeocodingResult googleResult) {
        this.address = address;
        this.latitude = googleResult.geometry.location.lat;
        this.longitude = googleResult.geometry.location.lng;
    }

    public GeocodingResult(@NotNull String address, @NotNull double lat, @NotNull double lng) {
        this.address = address;
        this.latitude = lat;
        this.longitude = lng;
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
