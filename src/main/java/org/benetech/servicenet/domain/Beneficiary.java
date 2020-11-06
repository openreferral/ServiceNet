package org.benetech.servicenet.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

/**
 * A Beneficiary.
 */
@Entity
@Table(name = "beneficiary")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Beneficiary extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Generated(GenerationTime.INSERT)
    @Column(name = "identifier", nullable = false, insertable = false)
    private Integer identifier;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "beneficiary", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Referral> referrals = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Beneficiary phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Referral> getReferrals() {
        return referrals;
    }

    public void setReferrals(Set<Referral> referrals) {
        this.referrals = referrals;
    }

    public Integer getIdentifier() {
        return this.identifier;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Beneficiary)) {
            return false;
        }
        return getId() != null && getId().equals(((Beneficiary) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Beneficiary{" +
            "id=" + getId() +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}
