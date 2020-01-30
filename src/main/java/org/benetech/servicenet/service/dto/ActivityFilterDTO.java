package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.benetech.servicenet.domain.enumeration.DateFilter;
import org.benetech.servicenet.domain.enumeration.SearchOn;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.ActivityFilter} entity.
 */
@Getter
@Setter
public class ActivityFilterDTO implements Serializable {

    private UUID id;

    private String name;

    private List<String> citiesFilterList = new ArrayList<>();

    private List<String> regionFilterList = new ArrayList<>();

    private List<String> postalCodesFilterList = new ArrayList<>();

    private List<String> taxonomiesFilterList = new ArrayList<>();

    private SearchOn searchOn;

    private List<String> searchFields = new ArrayList<>();

    private List<UUID> partnerFilterList = new ArrayList<>();

    private DateFilter dateFilter;

    private LocalDate fromDate;

    private LocalDate toDate;

    private Boolean hiddenFilter = false;

    private Boolean showPartner = false;

    private Boolean showOnlyHighlyMatched = false;

    private UUID userId;

    private String userLogin;

    private Boolean applyLocationSearch = false;

    private Double latitude;

    private Double longitude;

    private Double radius;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActivityFilterDTO activityFilterDTO = (ActivityFilterDTO) o;
        if (activityFilterDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), activityFilterDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActivityFilterDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", citiesFilterList='" + getCitiesFilterList() + "'" +
            ", regionFilterList='" + getRegionFilterList() + "'" +
            ", postalCodesFilterList='" + getPostalCodesFilterList() + "'" +
            ", taxonomiesFilterList='" + getTaxonomiesFilterList() + "'" +
            ", searchOn='" + getSearchOn() + "'" +
            ", searchFields='" + getSearchFields() + "'" +
            ", partnerFilterList='" + getPartnerFilterList() + "'" +
            ", dateFilter='" + getDateFilter() + "'" +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", hiddenFilter='" + getHiddenFilter() + "'" +
            ", showPartner='" + getShowPartner() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", applyLocationSearch='" + getApplyLocationSearch() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", radius='" + getRadius() + "'" +
            "}";
    }
}
