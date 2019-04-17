package org.benetech.servicenet.adapter.sheltertech.mapper;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.adapter.sheltertech.model.PhoneRaw;
import org.benetech.servicenet.domain.Phone;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ShelterTechPhoneMapper {

    ShelterTechPhoneMapper INSTANCE = Mappers.getMapper(ShelterTechPhoneMapper.class);

    @Named("mapToPhone")
    default Phone mapToPhone(PhoneRaw raw) {
        if (raw == null || StringUtils.isBlank(raw.getNumber())) {
            return null;
        }

        return toPhone(raw);
    }

    @IterableMapping(qualifiedByName = "mapToPhone")
    List<Phone> mapToPhones(List<PhoneRaw> phonesRaw);

    @Named("languageFromCountryCode")
    default String languageFromCountryCode(String countryCode) {
        if (StringUtils.equalsIgnoreCase("US", countryCode)) {
            return "en";
        }
        return null;
    }

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "number", target = "number")
    @Mapping(source = "extension", target = "extension")
    @Mapping(source = "serviceType", target = "type")
    @Mapping(source = "countryCode", target = "language", qualifiedByName = "languageFromCountryCode")
    @Mapping(ignore = true, target = "description")
    @Mapping(ignore = true, target = "contact")
    Phone toPhone(PhoneRaw raw);
}
