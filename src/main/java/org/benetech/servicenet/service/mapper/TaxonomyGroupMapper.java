package org.benetech.servicenet.service.mapper;

import java.util.UUID;
import org.benetech.servicenet.domain.TaxonomyGroup;
import org.benetech.servicenet.service.dto.TaxonomyGroupDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link TaxonomyGroup} and its DTO {@link TaxonomyGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {TaxonomyMapper.class})
public interface TaxonomyGroupMapper extends EntityMapper<TaxonomyGroupDTO, TaxonomyGroup> {

    @Mapping(target = "removeTaxonomies", ignore = true)

    default TaxonomyGroup fromId(UUID id) {
        if (id == null) {
            return null;
        }
        TaxonomyGroup taxonomyGroup = new TaxonomyGroup();
        taxonomyGroup.setId(id);
        return taxonomyGroup;
    }
}
