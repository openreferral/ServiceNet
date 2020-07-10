package org.benetech.servicenet.service.dto;

import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderRecordForMapDTO {

    private UUID id;

    private Set<GeocodingResultDTO> locations;
}
