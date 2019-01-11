package org.benetech.servicenet.adapter.healthleads;

import org.benetech.servicenet.adapter.healthleads.model.*;
import org.benetech.servicenet.domain.*;
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
    Location extractLocation(HealthleadsLocation location);

    @Mapping(target = "id", ignore = true)
    Organization extractOrganization(HealthleadsOrganization organization);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address1", source = "address")
    PhysicalAddress extractPhysicalAddress(HealthleadsPhysicalAddress physicalAddress);

    @Mapping(target = "id", ignore = true)
    RequiredDocument extractRequiredDocument(HealthleadsRequiredDocument requiredDocument);

    @Mapping(target = "id", ignore = true)
    Service extractService(HealthleadsService service);

    @Mapping(target = "id", ignore = true)
    ServiceAtLocation extractServiceAtLocation(HealthleadsServiceAtLocation serviceAtLocation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taxonomyDetails", source = "taxonomyDetail")
    ServiceTaxonomy extractServiceTaxonomy(HealthleadsServiceTaxonomy serviceTaxonomy);

    @Mapping(target = "id", ignore = true)
    Taxonomy extractTaxonomy(HealthleadsTaxonomy taxonomy);

    default Set<Language> extractLanguages(HealthleadsLanguage healthleadsLanguage) {
        String[] languages = healthleadsLanguage.getLanguage().split(LISTS_DELIMITER);
        return Arrays.stream(languages).map(language -> new Language().language(language.trim())).collect(Collectors.toSet());
    }

    default Set<Phone> extractPhones(Set<HealthleadsPhone> phones) {
        if (CollectionUtils.isEmpty(phones)) {
            return new HashSet<>();
        }

        Set<Phone> extractedPhones = new HashSet<>();
        for (HealthleadsPhone phoneToExtract : phones) {
            Phone phone = new Phone();

            phone.setNumber(phoneToExtract.getNumber());
            if (!StringUtils.isEmpty(phoneToExtract.getExtension())) {
                phone.setExtension(Integer.parseInt(phoneToExtract.getExtension()));
            }
            phone.setType(phoneToExtract.getType());
            phone.setLanguage(phoneToExtract.getLanguage());
            phone.setDescription(phoneToExtract.getDescription());

            extractedPhones.add(phone);
        }

        return extractedPhones;
    }

}
