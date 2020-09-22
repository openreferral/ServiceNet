package org.benetech.servicenet.service.dto.provider;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.benetech.servicenet.service.dto.ServiceDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleServiceDTO {

    private ServiceDTO service;

    public SimpleServiceDTO(UUID serviceId, String serviceName, UUID orgId) {
        this.service = new ServiceDTO(serviceId, serviceName, orgId);
    }
}
