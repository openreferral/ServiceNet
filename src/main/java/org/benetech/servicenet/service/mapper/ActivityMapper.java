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

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "metadata.id", target = "metadataId")
    @Mapping(source = "organization.id", target = "organizationId")
    ActivityDTO toDto(Activity activity);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "metadataId", target = "metadata")
    @Mapping(source = "organizationId", target = "organization")
    @Mapping(source = "reviewers", target = "reviewers", qualifiedByName = "mapReviewers")
    Activity toEntity(ActivityDTO activityDTO);

    @Named("mapReviewers")
    default Set<User> mapReviewers(Set<UserDTO> dtos) {
        if (dtos == null) {
            return null;
        }

        Set<User> users = new HashSet<>();
        for (UserDTO userDTO : dtos) {
            users.add(new User().id(userDTO.getId()));
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
