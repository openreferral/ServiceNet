package org.benetech.servicenet.service.dto;

import java.util.List;
import lombok.Data;
import org.benetech.servicenet.config.Constants;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Shelter;
import org.benetech.servicenet.domain.UserGroup;
import org.benetech.servicenet.domain.UserProfile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
@Data
public class UserDTO {

    private UUID id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 256)
    private String imageUrl;

    private boolean activated;

    @Size(min = 2, max = 6)
    private String langKey;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<String> authorities;

    private UUID systemAccountId;

    private String systemAccountName;

    private List<UUID> shelters;

    private List<UUID> organizations;

    private UUID siloId;

    private Boolean siloIsReferralEnabled;

    private String organizationName;

    private String organizationUrl;

    private String phoneNumber;

    private List<UUID> userGroups;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(UserProfile userProfile) {
        this.id = userProfile.getUserId();
        this.login = userProfile.getLogin();
        this.createdBy = userProfile.getCreatedBy();
        this.createdDate = userProfile.getCreatedDate();
        this.lastModifiedBy = userProfile.getLastModifiedBy();
        this.lastModifiedDate = userProfile.getLastModifiedDate();
        if (userProfile.getSilo() != null) {
            this.siloId = userProfile.getSilo().getId();
            this.siloIsReferralEnabled = userProfile.getSilo().isReferralEnabled();
        }
        if (userProfile.getSystemAccount() != null) {
            this.systemAccountId = userProfile.getSystemAccount().getId();
            this.systemAccountName = userProfile.getSystemAccount().getName();
        }
        if (userProfile.getShelters() != null) {
            this.shelters = userProfile.getShelters().stream()
                .map(Shelter::getId)
                .collect(Collectors.toList());
        }
        if (userProfile.getOrganizations() != null) {
            this.organizations = userProfile.getOrganizations().stream()
                .map(Organization::getId)
                .collect(Collectors.toList());
        }
        this.organizationName = userProfile.getOrganizationName();
        this.organizationUrl = userProfile.getOrganizationUrl();
        this.phoneNumber = userProfile.getPhoneNumber();
        if (userProfile.getUserGroups() != null) {
            this.userGroups = userProfile.getUserGroups().stream()
            .map(UserGroup::getId)
            .collect(Collectors.toList());
        }
    }
}
