package org.benetech.servicenet.service.dto.provider;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.benetech.servicenet.service.dto.PhysicalAddressDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleLocationDTO {

    private PhysicalAddressDTO physicalAddress;

    private UUID organizationId;

    public SimpleLocationDTO(UUID physicalAddressId, String physicalAddressCity,
        String physicalAddressStateProvince, String physicalAddressStateRegion, UUID orgId) {
        this.physicalAddress = new PhysicalAddressDTO(physicalAddressId, physicalAddressCity,
            physicalAddressStateProvince, physicalAddressStateRegion);
        this.organizationId = orgId;
    }
}
