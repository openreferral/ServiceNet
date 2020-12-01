package org.benetech.servicenet.service.dto.provider;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.benetech.servicenet.service.dto.AddressDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleLocationDTO {

    private UUID id;

    private String name;

    private AddressDTO physicalAddress;

    private UUID organizationId;

    public SimpleLocationDTO(UUID physicalAddressId, String physicalAddressCity,
        String physicalAddressStateProvince, String physicalAddressStateRegion, UUID orgId,
        UUID id, String name) {
        this.physicalAddress = new AddressDTO(physicalAddressId, physicalAddressCity,
            physicalAddressStateProvince, physicalAddressStateRegion);
        this.organizationId = orgId;
        this.id = id;
        this.name = name;
    }
}
