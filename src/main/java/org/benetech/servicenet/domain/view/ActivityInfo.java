package org.benetech.servicenet.domain.view;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity(name = "activity_info")
public class ActivityInfo {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "recent")
    private Timestamp recent;

    @Column(name = "recommended")
    private Long recommended;

    @Column(name = "account_id")
    private UUID accountId;
}
