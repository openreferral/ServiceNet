package org.benetech.servicenet.service.mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.UUID;
import org.benetech.servicenet.domain.Silo;
import org.benetech.servicenet.service.dto.SiloDTO;
import org.benetech.servicenet.service.dto.provider.SiloWithLogoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Silo} and its DTO {@link SiloDTO}.
 */
@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = IGNORE)
public interface SiloMapper extends EntityMapper<SiloDTO, Silo> {

    @Mapping(target = "userProfiles", ignore = true)
    Silo toEntity(SiloDTO siloDTO);

    @Mapping(target = "userProfiles", ignore = true)
    Silo toEntity(SiloWithLogoDTO siloWithLogoDTO);

    default Silo fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Silo silo = new Silo();
        silo.setId(id);
        return silo;
    }
}
