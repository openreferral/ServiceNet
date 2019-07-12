package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.MatchSimilarity;
import org.benetech.servicenet.service.dto.MatchSimilarityDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Mapper for the entity {@link MatchSimilarity} and its DTO {@link MatchSimilarityDTO}.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMatchMapper.class})
public interface MatchSimilarityMapper extends EntityMapper<MatchSimilarityDTO, MatchSimilarity> {

    @Mapping(source = "organizationMatch.id", target = "organizationMatchId")
    MatchSimilarityDTO toDto(MatchSimilarity matchSimilarity);

    @Mapping(source = "organizationMatchId", target = "organizationMatch")
    MatchSimilarity toEntity(MatchSimilarityDTO matchSimilarityDTO);

    default MatchSimilarity fromId(UUID id) {
        if (id == null) {
            return null;
        }
        MatchSimilarity matchSimilarity = new MatchSimilarity();
        matchSimilarity.setId(id);
        return matchSimilarity;
    }
}
