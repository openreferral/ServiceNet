package org.benetech.servicenet.adapter.healthleads;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsEligibility;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsLanguage;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsLocation;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsOrganization;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsPhone;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsPhysicalAddress;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsRequiredDocument;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsService;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsServiceAtLocation;
import org.benetech.servicenet.adapter.healthleads.model.HealthleadsServiceMetadata;
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
import org.benetech.servicenet.domain.ServiceMetadata;
import org.benetech.servicenet.domain.ServiceTaxonomy;
import org.benetech.servicenet.domain.Taxonomy;
import org.benetech.servicenet.service.mapper.IntegerMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { IntegerMapper.class })
public abstract class HealthLeadsDataMapper {

    public static final HealthLeadsDataMapper INSTANCE = Mappers.getMapper(HealthLeadsDataMapper.class);

    private Logger log = LoggerFactory.getLogger(HealthLeadsDataMapper.class);

    private static final String LISTS_DELIMITER = ";";

    public Optional<Eligibility> extractEligibility(HealthleadsEligibility eligibility) {
        if (StringUtils.isBlank(eligibility.getEligibility())) {
            return Optional.empty();
        }

        return Optional.of(toEligibility(eligibility));
    }

    public Location extractLocation(HealthleadsLocation location) {
        if (StringUtils.isBlank(location.getName())) {
            log.warn("Location name is empty for organization with ID: " + location.getOrganizationId());
        }

        return toLocation(location);
    }

    public Organization extractOrganization(HealthleadsOrganization organization) {
        if (StringUtils.isBlank(organization.getName())) {
            log.warn("organization name is empty for organization with ID: " + organization.getId());
        }

        return toOrganization(organization);
    }

    public Optional<PhysicalAddress> extractPhysicalAddress(HealthleadsPhysicalAddress physicalAddress) {
        if (StringUtils.isBlank(physicalAddress.getAddress())) {
            return Optional.empty();
        }

        return Optional.of(toPhysicalAddress(physicalAddress));
    }

    public Optional<RequiredDocument> extractRequiredDocument(HealthleadsRequiredDocument requiredDocument) {
        if (StringUtils.isBlank(requiredDocument.getDocument())) {
            return Optional.empty();
        }

        return Optional.of(toRequiredDocument(requiredDocument));
    }

    public Service extractService(HealthleadsService service) {
        Service srvc = toService(service);
        if (srvc.getName() == null) {
            srvc.setName("");
        }

        return srvc;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalDbId", source = "id")
    public abstract ServiceAtLocation extractServiceAtLocation(HealthleadsServiceAtLocation serviceAtLocation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taxonomyDetails", source = "taxonomyDetail")
    @Mapping(target = "externalDbId", source = "id")
    public abstract ServiceTaxonomy extractServiceTaxonomy(HealthleadsServiceTaxonomy serviceTaxonomy);

    public Optional<Taxonomy> extractTaxonomy(HealthleadsTaxonomy taxonomy) {
        if (StringUtils.isBlank(taxonomy.getName())) {
            return Optional.empty();
        }

        return Optional.of(toTaxonomy(taxonomy));
    }

    public Set<Language> extractLanguages(Set<HealthleadsLanguage> healthleadsLanguages) {
        return healthleadsLanguages.stream()
            .filter(lang -> StringUtils.isNotBlank(lang.getLanguage()))
            .flatMap(x -> Arrays.stream(x.getLanguage().split(LISTS_DELIMITER)))
            .map(language -> new Language()
            .language(language.trim()))
            .collect(Collectors.toSet());
    }

    public Set<ServiceMetadata> extractMetadata(Set<HealthleadsServiceMetadata> healthleadsServiceMetadata) {
        return healthleadsServiceMetadata.stream()
            .map(this::toMetadata)
            .collect(Collectors.toSet());
    }

    public Set<Phone> extractPhones(Set<HealthleadsPhone> phones) {
        return phones.stream()
            .filter(phone -> StringUtils.isNotBlank(phone.getNumber()))
            .map(this::toPhone)
            .collect(Collectors.toSet());
    }

    @Mapping(target = "id", ignore = true)
    protected abstract Eligibility toEligibility(HealthleadsEligibility eligibility);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalDbId", source = "id")
    protected abstract Location toLocation(HealthleadsLocation location);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalDbId", source = "id")
    protected abstract Organization toOrganization(HealthleadsOrganization organization);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalDbId", source = "id")
    protected abstract Service toService(HealthleadsService service);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address1", source = "address")
    protected abstract PhysicalAddress toPhysicalAddress(HealthleadsPhysicalAddress physicalAddress);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalDbId", source = "id")
    protected abstract RequiredDocument toRequiredDocument(HealthleadsRequiredDocument requiredDocument);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalDbId", source = "id")
    protected abstract Taxonomy toTaxonomy(HealthleadsTaxonomy taxonomy);

    @Mapping(target = "id", ignore = true)
    protected abstract Phone toPhone(HealthleadsPhone phone);

    @Mapping(target = "id", ignore = true)
    protected abstract ServiceMetadata toMetadata(HealthleadsServiceMetadata metadata);
}
