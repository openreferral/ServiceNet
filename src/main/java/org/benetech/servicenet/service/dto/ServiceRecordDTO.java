package org.benetech.servicenet.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRecordDTO {

    private ServiceDTO service;

    private Set<OpeningHoursDTO> regularScheduleOpeningHours;

    private Set<HolidayScheduleDTO> holidaySchedules;

    private FundingDTO funding;

    private EligibilityDTO eligibility;

    private Set<RequiredDocumentDTO> docs;

    private Set<PaymentAcceptedDTO> paymentsAccepteds;

    private Set<LanguageDTO> langs;

    private Set<ServiceTaxonomyDTO> taxonomies;

    private Set<PhoneDTO> phones;

    private Set<ContactDTO> contacts;
}
