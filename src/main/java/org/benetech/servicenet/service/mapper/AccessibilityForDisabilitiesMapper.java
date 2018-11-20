package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.AccessibilityForDisabilities;
import org.benetech.servicenet.service.dto.AccessibilityForDisabilitiesDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity AccessibilityForDisabilities and its DTO AccessibilityForDisabilitiesDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class})
public interface AccessibilityForDisabilitiesMapper
    extends EntityMapper<AccessibilityForDisabilitiesDTO, AccessibilityForDisabilities> {

    @Mapping(source = "location.id", target = "locationId")
    @Mapping(source = "location.name", target = "locationName")
    AccessibilityForDisabilitiesDTO toDto(AccessibilityForDisabilities accessibilityForDisabilities);

    @Mapping(source = "locationId", target = "location")
    AccessibilityForDisabilities toEntity(AccessibilityForDisabilitiesDTO accessibilityForDisabilitiesDTO);

    default AccessibilityForDisabilities fromId(Long id) {
        if (id == null) {
            return null;
        }
        AccessibilityForDisabilities accessibilityForDisabilities = new AccessibilityForDisabilities();
        accessibilityForDisabilities.setId(id);
        return accessibilityForDisabilities;
    }
}
