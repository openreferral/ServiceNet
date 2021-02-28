package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

/**
 * A ClientProfile.
 */
@Entity
@Table(name = "client_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClientProfile extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "client_id")
    private String clientId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NotNull
    @JsonIgnoreProperties("")
    private SystemAccount systemAccount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @NotNull
    @JsonIgnoreProperties("")
    private Silo silo;
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public String getClientId() {
        return clientId;
    }

    public ClientProfile clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientProfile)) {
            return false;
        }
        return getId() != null && getId().equals(((ClientProfile) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ClientProfile{" +
            "id=" + getId() +
            ", clientId='" + getClientId() + "'" +
            "}";
    }

    public SystemAccount getSystemAccount() {
        return systemAccount;
    }

    public void setSystemAccount(SystemAccount systemAccount) {
        this.systemAccount = systemAccount;
    }

    public Silo getSilo() {
        return silo;
    }

    public void setSilo(Silo silo) {
        this.silo = silo;
    }
}
