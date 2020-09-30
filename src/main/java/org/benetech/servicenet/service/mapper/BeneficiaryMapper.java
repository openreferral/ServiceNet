package org.benetech.servicenet.service.mapper;


import java.util.UUID;
import org.benetech.servicenet.domain.*;
import org.benetech.servicenet.service.dto.BeneficiaryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Beneficiary} and its DTO {@link BeneficiaryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BeneficiaryMapper extends EntityMapper<BeneficiaryDTO, Beneficiary> {



    default Beneficiary fromId(UUID id) {
        if (id == null) {
            return null;
        }
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(id);
        return beneficiary;
    }
}
