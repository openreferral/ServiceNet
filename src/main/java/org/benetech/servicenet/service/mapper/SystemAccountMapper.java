package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.service.dto.SystemAccountDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity SystemAccount and its DTO SystemAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SystemAccountMapper extends EntityMapper<SystemAccountDTO, SystemAccount> {

    default SystemAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        SystemAccount systemAccount = new SystemAccount();
        systemAccount.setId(id);
        return systemAccount;
    }
}
