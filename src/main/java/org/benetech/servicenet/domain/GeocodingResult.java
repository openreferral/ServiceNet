package org.benetech.servicenet.domain;

import com.google.maps.model.AddressComponentType;
import com.google.maps.model.AddressType;
import com.google.maps.model.LatLng;
import com.google.maps.model.LocationType;
import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Stream;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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

    @Column(name = "api_address")
    private String formattedAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "locationType")
    private LocationType locationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "addressType")
    private AddressType addressType;

    @Column(name = "streetNumber")
    private String streetNumber;

    @Column(name = "route")
    private String route;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "locality")
    private String locality;

    @Column(name = "area_level_1")
    private String administrativeAreaLevel1;

    @Column(name = "area_level_2")
    private String administrativeAreaLevel2;

    @Column(name = "country")
    private String country;

    @Column(name = "postalCode")
    private String postalCode;

    @Column(name = "premise")
    private String premise;

    @Column(name = "subpremise")
    private String subpremise;

    @Column(name = "partial_match")
    private Boolean partialMatch;

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

        this.formattedAddress = googleResult.formattedAddress;
        this.locationType = googleResult.geometry.locationType;
        this.addressType = googleResult.types[0];
        this.partialMatch = googleResult.partialMatch;

        this.streetNumber = getStringValueForAddressComponentType(googleResult,
            AddressComponentType.STREET_NUMBER);
        this.route = getStringValueForAddressComponentType(googleResult,
            AddressComponentType.ROUTE);
        this.neighborhood = getStringValueForAddressComponentType(googleResult,
            AddressComponentType.NEIGHBORHOOD);
        this.locality = getStringValueForAddressComponentType(googleResult,
            AddressComponentType.LOCALITY);
        this.administrativeAreaLevel1 = getStringValueForAddressComponentType(googleResult,
            AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1);
        this.administrativeAreaLevel2 = getStringValueForAddressComponentType(googleResult,
            AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2);
        this.country = getStringValueForAddressComponentType(googleResult,
            AddressComponentType.COUNTRY);
        this.postalCode = getStringValueForAddressComponentType(googleResult,
            AddressComponentType.POSTAL_CODE);
        this.premise = getStringValueForAddressComponentType(googleResult,
            AddressComponentType.PREMISE);
        this.subpremise = getStringValueForAddressComponentType(googleResult,
            AddressComponentType.SUBPREMISE);
    }

    public GeocodingResult(@NotNull String address, @NotNull double lat, @NotNull double lng) {
        this.address = address;
        this.latitude = lat;
        this.longitude = lng;
    }

// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    private String getStringValueForAddressComponentType(com.google.maps.model.GeocodingResult geocodingResult,
        AddressComponentType addressComponentType) {
        return Stream.of(geocodingResult.addressComponents)
            .filter(component -> component.types[0].equals(addressComponentType))
            .map(component -> component.longName)
            .findFirst()
            .orElse(null);
    }

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
