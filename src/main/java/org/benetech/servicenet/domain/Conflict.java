package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.benetech.servicenet.domain.enumeration.ConflictStateEnum;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

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
public class Conflict extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 5951338913885782058L;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "current_value", columnDefinition = "clob")
    private String currentValue;

    @Column(name = "current_value_date")
    private ZonedDateTime currentValueDate;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "offered_value", columnDefinition = "clob")
    private String offeredValue;

    @Column(name = "offered_value_date")
    private ZonedDateTime offeredValueDate;

    @Column(name = "field_name")
    private String fieldName;

    @Column(name = "entity_path")
    private String entityPath;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private ConflictStateEnum state;

    @Column(name = "state_date")
    private ZonedDateTime stateDate;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "resource_id")
    private UUID resourceId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private SystemAccount owner;

    @ManyToMany
    @Builder.Default
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "conflict_accepted_this_change",
               joinColumns = @JoinColumn(name = "conflicts_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "accepted_this_change_id", referencedColumnName = "id"))
    private Set<SystemAccount> acceptedThisChange = new HashSet<>();

    public Conflict id(UUID id) {
        this.setId(id);
        return this;
    }

    public Conflict addAcceptedThisChange(SystemAccount systemAccount) {
        this.acceptedThisChange.add(systemAccount);
        return this;
    }

    public Conflict removeAcceptedThisChange(SystemAccount systemAccount) {
        this.acceptedThisChange.remove(systemAccount);
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
