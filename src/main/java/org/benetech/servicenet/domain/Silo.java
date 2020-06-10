package org.benetech.servicenet.domain;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.GenericGenerator;

/**
 * A Silo.
 */
@Entity
@Table(name = "silo")
@AllArgsConstructor
@NoArgsConstructor
public class Silo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "silo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserProfile> userProfiles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Silo name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public Silo userProfiles(Set<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
        return this;
    }

    public Silo addUserProfiles(UserProfile userProfile) {
        this.userProfiles.add(userProfile);
        userProfile.setSilo(this);
        return this;
    }

    public Silo removeUserProfiles(UserProfile userProfile) {
        this.userProfiles.remove(userProfile);
        userProfile.setSilo(null);
        return this;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Silo)) {
            return false;
        }
        return id != null && id.equals(((Silo) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Silo{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
