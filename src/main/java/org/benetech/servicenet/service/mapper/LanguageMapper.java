package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.service.dto.LanguageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Mapper for the entity Language and its DTO LanguageDTO.
 */
@Mapper(componentModel = "spring", uses = {ServiceMapper.class, LocationMapper.class})
public interface LanguageMapper extends EntityMapper<LanguageDTO, Language> {

    @Mapping(source = "srvc.id", target = "srvcId")
    @Mapping(source = "srvc.name", target = "srvcName")
    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    LanguageDTO toDto(Language language);

    @Mapping(source = "srvcId", target = "srvc")
    @Mapping(source = "locationId", target = "location")
    Language toEntity(LanguageDTO languageDTO);

    default Language fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Language language = new Language();
        language.setId(id);
        return language;
    }
}
