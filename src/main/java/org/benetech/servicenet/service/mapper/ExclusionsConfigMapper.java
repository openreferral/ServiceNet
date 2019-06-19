package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.ExclusionsConfig;
import org.benetech.servicenet.service.dto.ExclusionsConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Mapper for the entity {@link ExclusionsConfig} and its DTO {@link ExclusionsConfigDTO}.
 */
@Mapper(componentModel = "spring", uses = {SystemAccountMapper.class})
public interface ExclusionsConfigMapper extends EntityMapper<ExclusionsConfigDTO, ExclusionsConfig> {

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "account.name", target = "accountName")
    ExclusionsConfigDTO toDto(ExclusionsConfig exclusionsConfig);

    @Mapping(source = "accountId", target = "account")
    @Mapping(target = "exclusions", ignore = true)
    @Mapping(target = "locationExclusions", ignore = true)
    ExclusionsConfig toEntity(ExclusionsConfigDTO exclusionsConfigDTO);

    default ExclusionsConfig fromId(UUID id) {
        if (id == null) {
            return null;
        }
        ExclusionsConfig exclusionsConfig = new ExclusionsConfig();
        exclusionsConfig.setId(id);
        return exclusionsConfig;
    }
}
