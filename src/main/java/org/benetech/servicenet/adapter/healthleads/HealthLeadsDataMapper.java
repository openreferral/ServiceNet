package org.benetech.servicenet.adapter.healthleads;

import org.benetech.servicenet.adapter.healthleads.model.HealthleadsEligibility;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsLanguage;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsLocation;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsOrganization;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsPhone;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsPhysicalAddress;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsRequiredDocument;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsService;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsServiceAtLocation;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsServiceTaxonomy;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsTaxonomy;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Location;
import org.benetech.servicenet.domain.Organization;
import org.benetech.servicenet.domain.Phone;
import org.benetech.servicenet.domain.PhysicalAddress;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.benetech.servicenet.domain.ServiceAtLocation;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HealthLeadsDataMapper {

    HealthLeadsDataMapper INSTANCE = Mappers.getMapper(HealthLeadsDataMapper.class);

    String LISTS_DELIMITER = ";";

    @Mapping(target = "id", ignore = true)
    Eligibility extractEligibility(HealthleadsEligibility eligibility);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalDbId", source = "id")
    Location extractLocation(HealthleadsLocation location);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalDbId", source = "id")
    Organization extractOrganization(HealthleadsOrganization organization);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address1", source = "address")
    PhysicalAddress extractPhysicalAddress(HealthleadsPhysicalAddress physicalAddress);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalDbId", source = "id")
    RequiredDocument extractRequiredDocument(HealthleadsRequiredDocument requiredDocument);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalDbId", source = "id")
    Service extractService(HealthleadsService service);

    @Mapping(target = "id", ignore = true)
    ServiceAtLocation extractServiceAtLocation(HealthleadsServiceAtLocation serviceAtLocation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taxonomyDetails", source = "taxonomyDetail")
    ServiceTaxonomy extractServiceTaxonomy(HealthleadsServiceTaxonomy serviceTaxonomy);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalDbId", source = "id")
    Taxonomy extractTaxonomy(HealthleadsTaxonomy taxonomy);

    default Set<Language> extractLanguages(Set<HealthleadsLanguage> healthleadsLanguages) {
        return healthleadsLanguages.stream()
            .flatMap(x -> Arrays.stream(x.getLanguage().split(LISTS_DELIMITER)))
            .map(language -> new Language()
            .language(language.trim()))
            .collect(Collectors.toSet());
    }

    default Set<Phone> extractPhones(Set<HealthleadsPhone> phones) {
        if (CollectionUtils.isEmpty(phones)) {
            return new HashSet<>();
        }

        Set<Phone> extractedPhones = new HashSet<>();
        for (HealthleadsPhone healthleadsPhone : phones) {
            Phone phone = new Phone();

            phone.setNumber(healthleadsPhone.getNumber());
            if (!StringUtils.isEmpty(healthleadsPhone.getExtension())) {
                phone.setExtension(Integer.parseInt(healthleadsPhone.getExtension()));
            }
            phone.setType(healthleadsPhone.getType());
            phone.setLanguage(healthleadsPhone.getLanguage());
            phone.setDescription(healthleadsPhone.getDescription());

            extractedPhones.add(phone);
        }

        return extractedPhones;
    }

}
