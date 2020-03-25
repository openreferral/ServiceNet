package org.benetech.servicenet.service.mapper;

import java.util.UUID;
import org.benetech.servicenet.domain.ActivityFilter;
import org.benetech.servicenet.service.dto.ActivityFilterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link ActivityFilter} and its DTO {@link ActivityFilterDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ActivityFilterMapper extends EntityMapper<ActivityFilterDTO, ActivityFilter> {

    @Mapping(source = "userProfile.id", target = "userId")
    @Mapping(source = "userProfile.login", target = "userLogin")
    ActivityFilterDTO toDto(ActivityFilter activityFilter);

    @Mapping(source = "userId", target = "userProfile")
    ActivityFilter toEntity(ActivityFilterDTO activityFilterDTO);

    default ActivityFilter fromId(UUID id) {
        if (id == null) {
            return null;
        }
        ActivityFilter activityFilter = new ActivityFilter();
        activityFilter.setId(id);
        return activityFilter;
    }
}
