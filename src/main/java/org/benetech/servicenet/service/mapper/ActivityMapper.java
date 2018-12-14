package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.Activity;
import org.benetech.servicenet.domain.User;
import org.benetech.servicenet.service.dto.ActivityDTO;
import org.benetech.servicenet.service.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Mapper for the entity Activity and its DTO ActivityDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, MetadataMapper.class, OrganizationMapper.class})
public interface ActivityMapper extends EntityMapper<ActivityDTO, Activity> {

    @Mapping(source = "metadata.user.id", target = "userId")
    @Mapping(source = "metadata.user.login", target = "userLogin")
    @Mapping(source = "metadata.id", target = "metadataId")
    @Mapping(source = "metadata.lastActionType", target = "metadataActionType")
    @Mapping(source = "metadata.lastActionDate", target = "metadataActionDate")
    @Mapping(source = "metadata.fieldName", target = "metadataFieldName")
    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "reviewers", target = "reviewers", qualifiedByName = "mapReviewersToLogin")
    ActivityDTO toDto(Activity activity);

    @Mapping(source = "metadataId", target = "metadata")
    @Mapping(source = "organizationId", target = "organization")
    @Mapping(source = "reviewers", target = "reviewers", qualifiedByName = "mapReviewersToUsers")
    Activity toEntity(ActivityDTO activityDTO);

    @Named("mapReviewersToLogin")
    default Set<UserDTO> mapReviewersToLogin(Set<User> users) {
        if (users == null) {
            return null;
        }

        Set<UserDTO> reviewers = new HashSet<>();
        for (User user : users) {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setLogin(user.getLogin());
            reviewers.add(dto);
        }

        return reviewers;
    }

    @Named("mapReviewersToUsers")
    default Set<User> mapReviewersToUsers(Set<UserDTO> reviewers) {
        if (reviewers == null) {
            return null;
        }

        Set<User> users = new HashSet<>();
        for (UserDTO dto : reviewers) {
            users.add(new User().id(dto.getId()));
        }

        return users;
    }

    default Activity fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Activity activity = new Activity();
        activity.setId(id);
        return activity;
    }
}
