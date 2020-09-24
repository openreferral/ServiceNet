package org.benetech.servicenet.service.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationRecordDTO {

    private LocationDTO location;

    private PhysicalAddressDTO physicalAddress;

    private PostalAddressDTO postalAddress;

    private Set<OpeningHoursDTO> regularScheduleOpeningHours;

    private Set<HolidayScheduleDTO> holidaySchedules;

    private Set<LanguageDTO> langs;

    private Set<AccessibilityForDisabilitiesDTO> accessibilities;

    private String regularScheduleNotes;

    public LocationRecordDTO(UUID physicalAddressId, String physicalAddressCity,
        String physicalAddressStateProvince, String physicalAddressStateRegion, UUID orgId) {
        this.location = new LocationDTO(orgId);
        this.physicalAddress = new PhysicalAddressDTO(physicalAddressId, physicalAddressCity,
            physicalAddressStateProvince, physicalAddressStateRegion);
    }
}
