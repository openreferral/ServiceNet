package org.benetech.servicenet.service.mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

import java.util.UUID;
import org.benetech.servicenet.domain.ClientProfile;
import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.service.dto.ClientProfileDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SystemAccountMapper.class}, unmappedTargetPolicy = IGNORE)
public interface ClientProfileMapper extends EntityMapper<ClientProfileDto, ClientProfile> {

    default ClientProfile fromId(UUID id) {
        if (id == null) {
            return null;
        }
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setId(id);
        return clientProfile;
    }

    default UUID map(SystemAccount systemAccount) {
        return systemAccount.getId();
    }
}
