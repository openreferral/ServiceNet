package org.benetech.servicenet.service.dto.provider;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.benetech.servicenet.service.dto.GeocodingResultDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderRecordForMapDTO {

    private UUID id;

    private GeocodingResultDTO location;

    public ProviderRecordForMapDTO(UUID orgId, UUID id, String address, Double latitude, Double longitude) {
        this.id = orgId;
        this.location = new GeocodingResultDTO(id, address, latitude, longitude);
    }
}
