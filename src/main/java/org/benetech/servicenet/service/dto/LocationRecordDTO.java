package org.benetech.servicenet.service.dto;

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
}
