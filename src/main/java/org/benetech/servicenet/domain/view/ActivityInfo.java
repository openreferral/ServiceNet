package org.benetech.servicenet.domain.view;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.OrganizationMatch;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "activity_info")
@Immutable
public class ActivityInfo {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "name", columnDefinition = "clob")
    private String name;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "alternate_name", columnDefinition = "clob")
    private String alternateName;

    @Column(name = "recent")
    private ZonedDateTime recent;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    @Column(name = "recommended")
    private Long recommended;

    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "similarity")
    private BigDecimal similarity;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Set<Organization> organizations;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_record_id")
    private Set<OrganizationMatch> organizationMatches;

    public Organization getOrganization() {
        return organizations.iterator().next();
    }
}
