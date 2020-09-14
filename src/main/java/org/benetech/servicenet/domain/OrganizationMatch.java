package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private UserProfile dismissedBy;

    @Column(name = "dismiss_date")
    private ZonedDateTime dismissDate;

    @Column(name = "hidden")
    private Boolean hidden = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserProfile hiddenBy;

    @Column(name = "hidden_date")
    private ZonedDateTime hiddenDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("")
    private Organization organizationRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("")
    private Organization partnerVersion;

    @Column(name = "similarity")
    private BigDecimal similarity;

    @OneToMany(mappedBy = "organizationMatch", cascade = CascadeType.REMOVE)
    private Set<MatchSimilarity> matchSimilarities;

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

    public Boolean isHidden() {
        return hidden;
    }

    public OrganizationMatch hidden(Boolean hidden) {
        this.hidden = hidden;
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

    public OrganizationMatch similarity(BigDecimal similarity) {
        this.similarity = similarity;
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
