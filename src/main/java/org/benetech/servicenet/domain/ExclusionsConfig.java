package org.benetech.servicenet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A ExclusionsConfig.
 */
@Entity
@Table(name = "exclusions_config")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExclusionsConfig extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @OneToOne(optional = false)
    @JoinColumn(unique = true)
    private SystemAccount account;

    @OneToMany(mappedBy = "config")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FieldExclusion> exclusions = new HashSet<>();

    @OneToMany(mappedBy = "config")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LocationExclusion> locationExclusions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public SystemAccount getAccount() {
        return account;
    }

    public ExclusionsConfig account(SystemAccount systemAccount) {
        this.account = systemAccount;
        return this;
    }

    public void setAccount(SystemAccount systemAccount) {
        this.account = systemAccount;
    }

    public Set<FieldExclusion> getExclusions() {
        return exclusions;
    }

    public ExclusionsConfig exclusions(Set<FieldExclusion> fieldExclusions) {
        this.exclusions = fieldExclusions;
        return this;
    }

    public ExclusionsConfig addExclusions(FieldExclusion fieldExclusion) {
        this.exclusions.add(fieldExclusion);
        fieldExclusion.setConfig(this);
        return this;
    }

    public ExclusionsConfig removeExclusions(FieldExclusion fieldExclusion) {
        this.exclusions.remove(fieldExclusion);
        fieldExclusion.setConfig(null);
        return this;
    }

    public void setExclusions(Set<FieldExclusion> fieldExclusions) {
        this.exclusions = fieldExclusions;
    }

    public Set<LocationExclusion> getLocationExclusions() {
        return locationExclusions;
    }

    public ExclusionsConfig locationExclusions(Set<LocationExclusion> locationExclusions) {
        this.locationExclusions = locationExclusions;
        return this;
    }

    public ExclusionsConfig addLocationExclusions(LocationExclusion locationExclusion) {
        this.locationExclusions.add(locationExclusion);
        locationExclusion.setConfig(this);
        return this;
    }

    public ExclusionsConfig removeLocationExclusions(LocationExclusion locationExclusion) {
        this.locationExclusions.remove(locationExclusion);
        locationExclusion.setConfig(null);
        return this;
    }

    public void setLocationExclusions(Set<LocationExclusion> locationExclusions) {
        this.locationExclusions = locationExclusions;
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
        ExclusionsConfig exclusionsConfig = (ExclusionsConfig) o;
        if (exclusionsConfig.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), exclusionsConfig.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
