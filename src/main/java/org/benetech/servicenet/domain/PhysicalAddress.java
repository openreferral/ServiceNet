package org.benetech.servicenet.domain;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PhysicalAddress.
 */
@SuppressWarnings("CPD-START")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "physical_address")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PhysicalAddress extends AbstractEntity implements Serializable, Address, DeepComparable {

    private static final long serialVersionUID = 1L;

    @Column(name = "attention")
    @Size(max = 255, message = "Field value is too long.")
    private String attention;

    @NotNull
    @Column(name = "address_1", nullable = false)
    @Size(max = 255, message = "Field value is too long.")
    private String address1 = "";

    @Column(name = "address_2")
    @Size(max = 255, message = "Field value is too long.")
    private String address2 = "";

    @NotNull
    @Column(name = "city", nullable = false)
    @Size(max = 255, message = "Field value is too long.")
    private String city = "";

    @Column(name = "region")
    @Size(max = 255, message = "Field value is too long.")
    private String region;

    @NotNull
    @Column(name = "state_province", nullable = false)
    @Size(max = 255, message = "Field value is too long.")
    private String stateProvince = "";

    @Column(name = "postal_code")
    @Size(max = 255, message = "Field value is too long.")
    private String postalCode;

    @Column(name = "country")
    @Size(max = 255, message = "Field value is too long.")
    private String country;

    @OneToOne
    @JoinColumn(unique = true)
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public PhysicalAddress attention(String attention) {
        this.attention = attention;
        return this;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public PhysicalAddress address1(String address1) {
        this.address1 = address1;
        return this;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public PhysicalAddress address2(String address2) {
        this.address2 = address2;
        return this;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public PhysicalAddress city(String city) {
        this.city = city;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public PhysicalAddress region(String region) {
        this.region = region;
        return this;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public PhysicalAddress stateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public PhysicalAddress postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public PhysicalAddress country(String country) {
        this.country = country;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public PhysicalAddress location(Location location) {
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
        PhysicalAddress physicalAddress = (PhysicalAddress) o;
        if (physicalAddress.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), physicalAddress.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PhysicalAddress{" +
            "id=" + getId() +
            ", attention='" + getAttention() + "'" +
            ", address1='" + getAddress1() + "'" +
            ", city='" + getCity() + "'" +
            ", region='" + getRegion() + "'" +
            ", stateProvince='" + getStateProvince() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", country='" + getCountry() + "'" +
            "}";
    }

    @Override
    public String getAddress() {
        return Stream.of(getAddress1(), getCity(), getCountry(), getPostalCode(), getRegion(), getStateProvince())
            .filter(StringUtils::isNotBlank).collect(Collectors.joining(DELIMITER));
    }

    @SuppressWarnings("checkstyle:booleanExpressionComplexity")
    @Override
    public boolean deepEquals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PhysicalAddress pa = (PhysicalAddress) o;
        return Objects.equals(attention, pa.attention) &&
            Objects.equals(address1, pa.address1) &&
            Objects.equals(address2, pa.address2) &&
            Objects.equals(city, pa.city) &&
            Objects.equals(region, pa.region) &&
            Objects.equals(stateProvince, pa.stateProvince) &&
            Objects.equals(postalCode, pa.postalCode) &&
            Objects.equals(country, pa.country);
    }
}
