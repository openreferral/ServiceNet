package org.benetech.servicenet.service.dto;

import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public ServiceRecordDTO(UUID serviceId, String serviceName, UUID orgId) {
        this.service = new ServiceDTO(serviceId, serviceName, orgId);
    }
}
