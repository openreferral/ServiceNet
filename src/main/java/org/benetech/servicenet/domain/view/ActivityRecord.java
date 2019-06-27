package org.benetech.servicenet.domain.view;

import lombok.Getter;
import lombok.Setter;
import org.benetech.servicenet.domain.Organization;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "activity_record")
@Immutable
public class ActivityRecord {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    @OneToOne
    @JoinColumn(name = "id")
    private Organization organization;
}
