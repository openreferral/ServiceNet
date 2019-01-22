package org.benetech.servicenet.adapter.sheltertech.mapper;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.sheltertech.model.ServiceRaw;
import org.benetech.servicenet.domain.Eligibility;
import org.benetech.servicenet.domain.RequiredDocument;
import org.benetech.servicenet.domain.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;

import static org.benetech.servicenet.adapter.sheltertech.ShelterTechConstants.PROVIDER_NAME;

@Mapper
public interface ShelterTechServiceMapper {

    ShelterTechServiceMapper INSTANCE = Mappers.getMapper(ShelterTechServiceMapper.class);

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "alternateName", target = "alternateName")
    @Mapping(source = "longDescription", target = "description")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "email", target = "email")
    @Mapping(ignore = true, target = "status", qualifiedByName = "statusFromCertified")
    @Mapping(source = "interpretationServices", target = "interpretationServices")
    @Mapping(source = "applicationProcess", target = "applicationProcess")
    @Mapping(source = "waitTime", target = "waitTime")
    @Mapping(source = "fee", target = "fees")
    @Mapping(ignore = true, target = "accreditations") // TODO
    @Mapping(ignore = true, target = "type") // TODO
    @Mapping(ignore = true, target = "updatedAt")
    @Mapping(source = "id", target = "externalDbId")
    @Mapping(constant = PROVIDER_NAME, target = "providerName")
    @Mapping(ignore = true, target = "organization") // TODO
    @Mapping(ignore = true, target = "program") // TODO
    @Mapping(ignore = true, target = "location")
    @Mapping(ignore = true, target = "regularSchedule")
    @Mapping(ignore = true, target = "holidaySchedule") // TODO
    @Mapping(ignore = true, target = "funding") // TODO
    @Mapping(source = "eligibility", target = "eligibility", qualifiedByName = "eligibilityFromString")
    @Mapping(ignore = true, target = "areas") // TODO
    @Mapping(source = "requiredDocuments", target = "docs", qualifiedByName = "docsFromString")
    @Mapping(ignore = true, target = "paymentsAccepteds") // TODO
    @Mapping(ignore = true, target = "langs") // TODO
    @Mapping(ignore = true, target = "taxonomies") // TODO
    @Mapping(ignore = true, target = "phones") // TODO
    Service mapToService(ServiceRaw serviceRaw);

    @Named("statusFromCertified")
    default String statusFromCertified(Boolean certified) {
        if (certified) {
            return "Certified";
        } else {
            return "Non-certified";
        }
    }

    @Named("eligibilityFromString")
    default Eligibility eligibilityFromString(String eligibilityString) {
        if (StringUtils.isBlank(eligibilityString)) {
            return null;
        }

       return Eligibility.builder()
           .eligibility(eligibilityString)
           .build();
    }

    @Named("docsFromString")
    default Set<RequiredDocument> docsFromString(String requiredDocumentsString) {
        Set<RequiredDocument> requiredDocuments = new HashSet<>();
        if (StringUtils.isBlank(requiredDocumentsString)) {
            return requiredDocuments;
        }

        requiredDocuments.add(RequiredDocument.builder()
            .document(requiredDocumentsString)
            .providerName(PROVIDER_NAME)
            .build());

        return requiredDocuments;
    }

}
