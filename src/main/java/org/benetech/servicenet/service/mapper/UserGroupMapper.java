package org.benetech.servicenet.service.mapper;


import java.util.UUID;
import org.benetech.servicenet.domain.*;
import org.benetech.servicenet.service.dto.UserGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserGroup} and its DTO {@link UserGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {SiloMapper.class})
public interface UserGroupMapper extends EntityMapper<UserGroupDTO, UserGroup> {

    @Mapping(source = "silo.id", target = "siloId")
    UserGroupDTO toDto(UserGroup userGroup);

    @Mapping(source = "siloId", target = "silo")
    UserGroup toEntity(UserGroupDTO userGroupDTO);

    default UserGroup fromId(UUID id) {
        if (id == null) {
            return null;
        }
        UserGroup userGroup = new UserGroup();
        userGroup.setId(id);
        return userGroup;
    }
}
