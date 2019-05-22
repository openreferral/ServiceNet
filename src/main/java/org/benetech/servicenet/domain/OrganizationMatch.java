package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A OrganizationMatch.
 */
@Entity
@Data
@Table(name = "organization_match")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrganizationMatch extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "jhi_timestamp")
    private ZonedDateTime timestamp;

    @Column(name = "dismissed")
    private Boolean dismissed = false;

    @Column(name = "dismiss_comment")
    private String dismissComment;

    @ManyToOne
    private User dismissedBy;

    @Column(name = "dismiss_date")
    private ZonedDateTime dismissDate;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Organization organizationRecord;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Organization partnerVersion;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public OrganizationMatch timestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Boolean isDismissed() {
        return dismissed;
    }

    public OrganizationMatch dismissed(Boolean dismissed) {
        this.dismissed = dismissed;
        return this;
    }

    public OrganizationMatch organizationRecord(Organization organization) {
        this.organizationRecord = organization;
        return this;
    }

    public OrganizationMatch partnerVersion(Organization organization) {
        this.partnerVersion = organization;
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
        OrganizationMatch organizationMatch = (OrganizationMatch) o;
        if (organizationMatch.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), organizationMatch.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
