package org.benetech.servicenet.domain;

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
 * A PostalAddress.
 */
@Entity
@Table(name = "postal_address")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PostalAddress extends AbstractEntity implements Serializable {

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

    public PostalAddress attention(String attention) {
        this.attention = attention;
        return this;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public PostalAddress address1(String address1) {
        this.address1 = address1;
        return this;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public PostalAddress address2(String address2) {
        this.address2 = address2;
        return this;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public PostalAddress city(String city) {
        this.city = city;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public PostalAddress region(String region) {
        this.region = region;
        return this;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public PostalAddress stateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public PostalAddress postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public PostalAddress country(String country) {
        this.country = country;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public PostalAddress location(Location location) {
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
        PostalAddress postalAddress = (PostalAddress) o;
        if (postalAddress.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), postalAddress.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PostalAddress{" +
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
}
