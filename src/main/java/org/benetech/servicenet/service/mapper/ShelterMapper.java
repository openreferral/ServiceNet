package org.benetech.servicenet.service.mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.UUID;
import org.benetech.servicenet.domain.Shelter;
import org.benetech.servicenet.service.dto.ShelterDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Shelter and its DTO ShelterDTO.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class}, unmappedTargetPolicy = IGNORE)
public interface ShelterMapper extends EntityMapper<ShelterDTO, Shelter> {

    ShelterDTO toDto(Shelter shelter);

    Shelter toEntity(ShelterDTO shelterDTO);

    default Shelter fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Shelter shelter = new Shelter();
        shelter.setId(id);
        return shelter;
    }
}
