package org.benetech.servicenet.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the PhysicalAddress entity.
 */
public class PhysicalAddressDTO implements Serializable {

    private UUID id;

    private String attention;

    @NotNull
    private String address1;

    private String address2;

    @NotNull
    private String city;

    private String region;

    @NotNull
    private String stateProvince;

    @NotNull
    private String postalCode;

    @NotNull
    private String country;

    private UUID locationId;

    private String locationName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public UUID getLocationId() {
        return locationId;
    }

    public void setLocationId(UUID locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PhysicalAddressDTO physicalAddressDTO = (PhysicalAddressDTO) o;
        if (physicalAddressDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), physicalAddressDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PhysicalAddressDTO{" +
            "id=" + getId() +
            ", attention='" + getAttention() + "'" +
            ", address1='" + getAddress1() + "'" +
            ", city='" + getCity() + "'" +
            ", region='" + getRegion() + "'" +
            ", stateProvince='" + getStateProvince() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", country='" + getCountry() + "'" +
            ", location=" + getLocationId() +
            ", location='" + getLocationName() + "'" +
            "}";
    }
}
