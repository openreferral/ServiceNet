package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.benetech.servicenet.config.Constants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

/**
 * A user.
 */
@Entity
@Table(name = "user_profile")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    // external user id
    @Column(name = "user_id")
    private UUID userId;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_account_id")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnoreProperties("")
    private SystemAccount systemAccount;

    @JsonIgnore
    @ManyToMany(mappedBy = "userProfiles", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Shelter> shelters;

    @JsonIgnore
    @ManyToMany(mappedBy = "userProfiles", fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Organization> organizations;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "filter_id")
    private ActivityFilter filter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("")
    private Silo silo;

    @JsonIgnore
    @ManyToMany(mappedBy = "userProfiles", fetch = FetchType.EAGER)
    private Set<UserGroup> userGroups = new HashSet<>();

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "organization_name", columnDefinition = "clob")
    private String organizationName;

    @Column(name = "organization_url")
    @Pattern(regexp = Constants.URL_REGEX)
    private String organizationUrl;

    @Column(name = "phone_number")
    @Pattern(regexp = Constants.PHONE_REGEX)
    private String phoneNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserProfile userProfile = (UserProfile) o;
        return !(userProfile.getId() == null || getId() == null) && Objects.equals(getId(), userProfile
            .getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserProfile{" +
            "id='" + id + '\'' +
            ", userId='" + userId + '\'' +
            "}";
    }
}
