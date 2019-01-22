package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.Program;
import org.benetech.servicenet.service.dto.ProgramDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity Program and its DTO ProgramDTO.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class}, unmappedTargetPolicy = IGNORE)
public interface ProgramMapper extends EntityMapper<ProgramDTO, Program> {

    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organizationName")
    ProgramDTO toDto(Program program);

    @Mapping(source = "organizationId", target = "organization")
    @Mapping(target = "services", ignore = true)
    Program toEntity(ProgramDTO programDTO);

    default Program fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Program program = new Program();
        program.setId(id);
        return program;
    }
}
