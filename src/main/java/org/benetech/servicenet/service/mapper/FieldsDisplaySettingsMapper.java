package org.benetech.servicenet.service.mapper;

import java.util.UUID;
import org.benetech.servicenet.domain.FieldsDisplaySettings;
import org.benetech.servicenet.service.dto.FieldsDisplaySettingsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link FieldsDisplaySettings} and its DTO {@link FieldsDisplaySettingsDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface FieldsDisplaySettingsMapper extends EntityMapper<FieldsDisplaySettingsDTO, FieldsDisplaySettings> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    FieldsDisplaySettingsDTO toDto(FieldsDisplaySettings fieldsDisplaySettings);

    @Mapping(source = "userId", target = "user")
    FieldsDisplaySettings toEntity(FieldsDisplaySettingsDTO fieldsDisplaySettingsDTO);

    default FieldsDisplaySettings fromId(UUID id) {
        if (id == null) {
            return null;
        }
        FieldsDisplaySettings fieldsDisplaySettings = new FieldsDisplaySettings();
        fieldsDisplaySettings.setId(id);
        return fieldsDisplaySettings;
    }
}
