package org.benetech.servicenet.service.mapper;

import org.benetech.servicenet.domain.PaymentAccepted;
import org.benetech.servicenet.service.dto.PaymentAcceptedDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapper for the entity PaymentAccepted and its DTO PaymentAcceptedDTO.
 */
@Mapper(componentModel = "spring", uses = {ServiceMapper.class}, unmappedTargetPolicy = IGNORE)
public interface PaymentAcceptedMapper extends EntityMapper<PaymentAcceptedDTO, PaymentAccepted> {

    @Mapping(source = "srvc.id", target = "srvcId")
    @Mapping(source = "srvc.name", target = "srvcName")
    PaymentAcceptedDTO toDto(PaymentAccepted paymentAccepted);

    @Mapping(source = "srvcId", target = "srvc")
    PaymentAccepted toEntity(PaymentAcceptedDTO paymentAcceptedDTO);

    default PaymentAccepted fromId(UUID id) {
        if (id == null) {
            return null;
        }
        PaymentAccepted paymentAccepted = new PaymentAccepted();
        paymentAccepted.setId(id);
        return paymentAccepted;
    }
}
