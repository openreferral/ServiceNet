package org.benetech.servicenet.adapter.icarol;

import org.apache.commons.lang3.BooleanUtils;
import org.benetech.servicenet.adapter.icarol.model.ICarolContact;
import org.benetech.servicenet.adapter.icarol.model.ICarolContactDetails;
import org.benetech.servicenet.adapter.icarol.model.ICarolName;
import org.benetech.servicenet.adapter.shared.util.LocationUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;

import static org.mapstruct.ReportingPolicy.IGNORE;

/**
 * Mapping for ICarolData confidential fields
 * IMPORTANT: Some of ICarol object might be confidential, but are mapped to a single field in the ServiceNet.
 *            In this case we need to manually set those fields to null.
 */
@Mapper(unmappedTargetPolicy = IGNORE)
public interface ICarolConfidentialFieldsMapper {

    ICarolConfidentialFieldsMapper INSTANCE = Mappers.getMapper(ICarolConfidentialFieldsMapper.class);
    String PRIMARY = "Primary";
    String EMAIL_ADDRESS = "EmailAddress";
    String WEBSITE = "Website";

    @Named("locationName")
    default String extractLocationNameIfNotConfidential(ICarolContact contact) {
        if (BooleanUtils.isTrue(contact.getIsConfidential())) {
            return null;
        } else {
            return LocationUtils.buildLocationName(contact.getCity(), contact.getStateProvince(), contact.getLine1());
        }
    }

    @Named("email")
    default String extractEmailIfNotConfidential(ICarolContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(EMAIL_ADDRESS)).findFirst()
            .filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential()))
            .map(entry -> entry.getContact().getAddress().replace(" ", ""))
            .orElse(null);
    }

    @Named("url")
    default String extractUrlIfNotConfidential(ICarolContactDetails[] contactDetails) {
        return Arrays.stream(contactDetails)
            .filter(entry -> entry.getContact().getType().equals(WEBSITE)).findFirst()
            .filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential()))
            .map(entry -> entry.getContact().getUrl().replace(" ", ""))
            .orElse(null);
    }

    @Named("name")
    default String extractNameIfNotConfidential(ICarolName[] names) {
        return Arrays.stream(names)
            .filter(name -> !name.getPurpose().equals(PRIMARY)).findFirst()
            .filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential()))
            .map(ICarolName::getValue)
            .orElse(null);
    }

    @Named("alternateName")
    default String extractAlternateNameIfNotConfidential(ICarolName[] names) {
        return Arrays.stream(names)
            .filter(name -> !name.getPurpose().equals(PRIMARY)).skip(1).findFirst()
            .filter(x -> BooleanUtils.isNotTrue(x.getIsConfidential()))
            .map(ICarolName::getValue)
            .orElse(null);
    }
}
