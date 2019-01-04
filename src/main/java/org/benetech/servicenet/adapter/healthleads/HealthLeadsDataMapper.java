package org.benetech.servicenet.adapter.healthleads;

import org.benetech.servicenet.domain.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HealthLeadsDataMapper {

    HealthLeadsDataMapper INSTANCE = Mappers.getMapper(HealthLeadsDataMapper.class);

    String LISTS_DELIMITER = ";";

    @Mapping(target = "id", ignore = true)
    Eligibility extractEligibility(org.benetech.servicenet.adapter.healthleads.model.Eligibility eligibility);

    @Mapping(target = "id", ignore = true)
    Location extractLocation(org.benetech.servicenet.adapter.healthleads.model.Location location);

    @Mapping(target = "id", ignore = true)
    Organization extractOrganization(org.benetech.servicenet.adapter.healthleads.model.Organization organization);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address1", source = "address")
    PhysicalAddress extractPhysicalAddress(org.benetech.servicenet.adapter.healthleads.model.PhysicalAddress physicalAddress);

    @Mapping(target = "id", ignore = true)
    RequiredDocument extractRequiredDocument(org.benetech.servicenet.adapter.healthleads.model.RequiredDocument requiredDocument);

    @Mapping(target = "id", ignore = true)
    Service extractService(org.benetech.servicenet.adapter.healthleads.model.Service service);

    @Mapping(target = "id", ignore = true)
    ServiceAtLocation extractServiceAtLocation(org.benetech.servicenet.adapter.healthleads.model.ServiceAtLocation serviceAtLocation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taxonomyDetails", source = "taxonomyDetail")
    ServiceTaxonomy extractServiceTaxonomy(org.benetech.servicenet.adapter.healthleads.model.ServiceTaxonomy serviceTaxonomy);

    @Mapping(target = "id", ignore = true)
    Taxonomy extractTaxonomy(org.benetech.servicenet.adapter.healthleads.model.Taxonomy taxonomy);

    default Set<Language> extractLanguages(org.benetech.servicenet.adapter.healthleads.model.Language healthleadsLanguage) {
        String[] languages = healthleadsLanguage.getLanguage().split(LISTS_DELIMITER);
        return Arrays.stream(languages).map(language -> new Language().language(language.trim())).collect(Collectors.toSet());
    }

    default Phone extractPhone(org.benetech.servicenet.adapter.healthleads.model.Phone phone) {
        if (phone == null) {
            return null;
        }

        Phone phone1 = new Phone();

        phone1.setNumber( phone.getNumber() );
        if (phone.getExtension() != null && !phone.getExtension().isEmpty()) {
            phone1.setExtension(Integer.parseInt(phone.getExtension()));
        }
        phone1.setType(phone.getType());
        phone1.setLanguage(phone.getLanguage());
        phone1.setDescription(phone.getDescription());

        return phone1;
    }

}
