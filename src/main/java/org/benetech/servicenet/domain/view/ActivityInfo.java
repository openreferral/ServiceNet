package org.benetech.servicenet.domain.view;

import javax.persistence.Lob;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.UUID;
import org.hibernate.annotations.Type;

@Data
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
    private Timestamp recent;

    @Column(name = "recommended")
    private Long recommended;

    @Column(name = "account_id")
    private UUID accountId;
}
