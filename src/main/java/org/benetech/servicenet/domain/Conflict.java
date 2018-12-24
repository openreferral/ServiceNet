package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;

import org.benetech.servicenet.domain.enumeration.ConflictStateEnum;
import org.hibernate.annotations.GenericGenerator;

/**
 * A Conflict.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conflict")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Conflict implements Serializable {

    private static final long serialVersionUID = 5951338913885782058L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "current_value")
    private String currentValue;

    @Column(name = "current_value_date")
    private Instant currentValueDate;

    @Column(name = "offered_value")
    private String offeredValue;

    @Column(name = "offered_value_date")
    private Instant offeredValueDate;

    @Column(name = "field_name")
    private String fieldName;

    @Column(name = "entity_path")
    private String entityPath;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private ConflictStateEnum state;

    @Column(name = "state_date")
    private Instant stateDate;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "resource_id")
    private UUID resourceId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private SystemAccount owner;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "conflict_accepted_this_change",
               joinColumns = @JoinColumn(name = "conflicts_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "accepted_this_changes_id", referencedColumnName = "id"))
    private Set<SystemAccount> acceptedThisChanges = new HashSet<>();

    public Conflict addAcceptedThisChange(SystemAccount systemAccount) {
        this.acceptedThisChanges.add(systemAccount);
        return this;
    }

    public Conflict removeAcceptedThisChange(SystemAccount systemAccount) {
        this.acceptedThisChanges.remove(systemAccount);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Conflict conflict = (Conflict) o;
        if (conflict.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), conflict.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
