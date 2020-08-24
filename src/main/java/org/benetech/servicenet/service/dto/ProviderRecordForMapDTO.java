package org.benetech.servicenet.service.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.benetech.servicenet.domain.GeocodingResult;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderRecordForMapDTO {

    private UUID id;

    private GeocodingResultDTO location;

    public ProviderRecordForMapDTO(UUID id, GeocodingResult geocodingResult) {
        this.id = id;
        this.location = new GeocodingResultDTO(geocodingResult.getId(), geocodingResult.getAddress(),
            geocodingResult.getLatitude(), geocodingResult.getLongitude());
    }
}
