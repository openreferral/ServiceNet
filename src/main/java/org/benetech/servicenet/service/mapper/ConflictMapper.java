package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.Conflict;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.service.dto.ConflictDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity Conflict and its DTO ConflictDTO.
 */
@Mapper(componentModel = "spring", uses = {SystemAccountMapper.class}, unmappedTargetPolicy = IGNORE)
public interface ConflictMapper extends EntityMapper<ConflictDTO, Conflict> {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.name", target = "ownerName")
    @Mapping(source = "acceptedThisChange", target = "firstAcceptedId", qualifiedByName = "fromListToFirstAcceptedId")
    @Mapping(source = "acceptedThisChange", target = "firstAcceptedName", qualifiedByName = "fromListToFirstAcceptedName")
    ConflictDTO toDto(Conflict conflict);

    @Mapping(source = "ownerId", target = "owner")
    Conflict toEntity(ConflictDTO conflictDTO);

    default Conflict fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Conflict conflict = new Conflict();
        conflict.setId(id);
        return conflict;
    }

    @Named("fromListToFirstAcceptedId")
    default UUID fromListToFirstAcceptedId(Set<SystemAccount> acceptedThisChange) {
        if (CollectionUtils.isEmpty(acceptedThisChange)) {
            return null;
        } else {
            Iterator<SystemAccount> iterator = acceptedThisChange.iterator();
            SystemAccount account = iterator.next();
            return account.getId();
        }
    }

    @Named("fromListToFirstAcceptedName")
    default String fromListToFirstAcceptedName(Set<SystemAccount> acceptedThisChange) {
        if (CollectionUtils.isEmpty(acceptedThisChange)) {
            return null;
        } else {
            SystemAccount account = (acceptedThisChange.iterator()).next();
            return account.getName();
        }
    }
}
