package org.benetech.servicenet.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.Address;
import org.benetech.servicenet.domain.Beds;
import org.benetech.servicenet.domain.GeocodingResult;
import org.benetech.servicenet.domain.Option;
import org.benetech.servicenet.domain.Phone;

/**
 * A DTO for the Shelter entity.
 */
public class ShelterDTO implements Serializable, Address {

    @Getter
    @Setter
    private UUID id;

    @Getter
    @Setter
    private String agencyName;

    @Getter
    @Setter
    private String programName;

    @Getter
    @Setter
    private String alternateName;

    @Getter
    @Setter
    private String website;

    @Getter
    @Setter
    private String eligibilityDetails;

    @Getter
    @Setter
    private String documentsRequired;

    @Getter
    @Setter
    private String applicationProcess;

    @Getter
    @Setter
    private String fees;

    @Getter
    @Setter
    private String programHours;

    @Getter
    @Setter
    private String holidaySchedule;

    @Getter
    @Setter
    private List<String> emails;

    @Getter
    @Setter
    private String address1;

    @Getter
    @Setter
    private String address2;

    @Getter
    @Setter
    private String city;

    @Getter
    @Setter
    private String zipcode;

    @Getter
    @Setter
    private String locationDescription;

    @Getter
    @Setter
    private String busService;

    @Getter
    @Setter
    private String transportation;

    @Getter
    @Setter
    private String disabilityAccess;

    @Getter
    @Setter
    private Set<Option> tags = new HashSet<>();

    @JsonIgnoreProperties("shelter")
    @OneToMany(mappedBy = "shelter")
    @Getter
    @Setter
    private Set<Phone> phones = new HashSet<>();
    
    @JsonIgnoreProperties("shelter")
    @Getter
    @Setter
    private Beds beds;

    @JsonIgnoreProperties("shelters")
    @Getter
    @Setter
    private Set<Option> languages;

    @JsonIgnoreProperties("shelters")
    @Getter
    @Setter
    private Set<Option> definedCoverageAreas;

    @Getter
    @Setter
    private List<GeocodingResult> geocodingResults;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShelterDTO shelterDTO = (ShelterDTO) o;
        if (shelterDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shelterDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShelterDTO{" +
            "id=" + getId() +
            ", agencyName='" + getAgencyName() + "'" +
            ", programName='" + getProgramName() + "'" +
            ", alternateName='" + getAlternateName() + "'" +
            ", website='" + getWebsite() + "'" +
            ", eligibilityDetails='" + getEligibilityDetails() + "'" +
            ", documentsRequired='" + getDocumentsRequired() + "'" +
            ", applicationProcess='" + getApplicationProcess() + "'" +
            ", fees='" + getFees() + "'" +
            ", programHours='" + getProgramHours() + "'" +
            ", holidaySchedule='" + getHolidaySchedule() + "'" +
            ", emails='" + getEmails() + "'" +
            ", address1='" + getAddress1() + "'" +
            ", address2='" + getAddress2() + "'" +
            ", city='" + getCity() + "'" +
            ", zipcode='" + getZipcode() + "'" +
            ", locationDescription='" + getLocationDescription() + "'" +
            ", busService='" + getBusService() + "'" +
            ", transportation='" + getTransportation() + "'" +
            ", disabilityAccess='" + getDisabilityAccess() + "'" +
            ", languages='" + getLanguages() + "'" +
            ", phones='" + getPhones() + "'" +
            "}";
    }

    @Override
    public String getAddress() {
        return Stream.of(getAddress1(), getAddress2(), getCity(), getZipcode())
            .filter(StringUtils::isNotBlank).collect(Collectors.joining(DELIMITER));
    }
}
