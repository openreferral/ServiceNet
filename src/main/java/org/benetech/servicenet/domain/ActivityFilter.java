package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.benetech.servicenet.domain.enumeration.DateFilter;
import org.benetech.servicenet.domain.enumeration.SearchOn;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ActivityFilter.
 */
@Entity
@Table(name = "activity_filter")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ActivityFilter extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "name")
    private String name;

    @ElementCollection
    @CollectionTable(name = "city_filter", joinColumns = @JoinColumn(name = "filter_id"))
    @Column(name = "city")
    private List<String> citiesFilterList;

    @ElementCollection
    @CollectionTable(name = "region_filter", joinColumns = @JoinColumn(name = "filter_id"))
    @Column(name = "region")
    private List<String> regionFilterList;

    @ElementCollection
    @CollectionTable(name = "postal_code_filter", joinColumns = @JoinColumn(name = "filter_id"))
    @Column(name = "postal_code")
    private List<String> postalCodesFilterList;

    @ElementCollection
    @CollectionTable(name = "taxonomy_filter", joinColumns = @JoinColumn(name = "filter_id"))
    @Column(name = "taxonomy")
    private List<String> taxonomiesFilterList;

    @Enumerated(EnumType.STRING)
    @Column(name = "search_on")
    private SearchOn searchOn;

    @ElementCollection
    @CollectionTable(name = "search_field", joinColumns = @JoinColumn(name = "filter_id"))
    @Column(name = "field")
    private List<String> searchFields;

    @ElementCollection
    @CollectionTable(name = "partner_filter", joinColumns = @JoinColumn(name = "filter_id"))
    @Column(name = "partner")
    private List<UUID> partnerFilterList;

    @Enumerated(EnumType.STRING)
    @Column(name = "date_filter")
    private DateFilter dateFilter;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Column(name = "hidden_filter")
    private Boolean hiddenFilter;

    @Column(name = "show_partner")
    private Boolean showPartner;

    @ManyToOne
    @JsonIgnoreProperties("filters")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getName() {
        return name;
    }

    public ActivityFilter name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCitiesFilterList() {
        return citiesFilterList;
    }

    public ActivityFilter citiesFilterList(List<String> citiesFilterList) {
        this.citiesFilterList = citiesFilterList;
        return this;
    }

    public void setCitiesFilterList(List<String> citiesFilterList) {
        this.citiesFilterList = citiesFilterList;
    }

    public List<String> getRegionFilterList() {
        return regionFilterList;
    }

    public ActivityFilter regionFilterList(List<String> regionFilterList) {
        this.regionFilterList = regionFilterList;
        return this;
    }

    public void setRegionFilterList(List<String> regionFilterList) {
        this.regionFilterList = regionFilterList;
    }

    public List<String> getPostalCodesFilterList() {
        return postalCodesFilterList;
    }

    public ActivityFilter postalCodesFilterList(List<String> postalCodesFilterList) {
        this.postalCodesFilterList = postalCodesFilterList;
        return this;
    }

    public void setPostalCodesFilterList(List<String> postalCodesFilterList) {
        this.postalCodesFilterList = postalCodesFilterList;
    }

    public List<String> getTaxonomiesFilterList() {
        return taxonomiesFilterList;
    }

    public ActivityFilter taxonomiesFilterList(List<String> taxonomiesFilterList) {
        this.taxonomiesFilterList = taxonomiesFilterList;
        return this;
    }

    public void setTaxonomiesFilterList(List<String> taxonomiesFilterList) {
        this.taxonomiesFilterList = taxonomiesFilterList;
    }

    public SearchOn getSearchOn() {
        return searchOn;
    }

    public ActivityFilter searchOn(SearchOn searchOn) {
        this.searchOn = searchOn;
        return this;
    }

    public void setSearchOn(SearchOn searchOn) {
        this.searchOn = searchOn;
    }

    public List<String> getSearchFields() {
        return searchFields;
    }

    public ActivityFilter searchFields(List<String> searchFields) {
        this.searchFields = searchFields;
        return this;
    }

    public void setSearchFields(List<String> searchFields) {
        this.searchFields = searchFields;
    }

    public List<UUID> getPartnerFilterList() {
        return partnerFilterList;
    }

    public ActivityFilter partnerFilterList(List<UUID> partnerFilterList) {
        this.partnerFilterList = partnerFilterList;
        return this;
    }

    public void setPartnerFilterList(List<UUID> partnerFilterList) {
        this.partnerFilterList = partnerFilterList;
    }

    public DateFilter getDateFilter() {
        return dateFilter;
    }

    public ActivityFilter dateFilter(DateFilter dateFilter) {
        this.dateFilter = dateFilter;
        return this;
    }

    public void setDateFilter(DateFilter dateFilter) {
        this.dateFilter = dateFilter;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public ActivityFilter fromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public ActivityFilter toDate(LocalDate toDate) {
        this.toDate = toDate;
        return this;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Boolean isHiddenFilter() {
        return hiddenFilter;
    }

    public ActivityFilter hiddenFilter(Boolean hiddenFilter) {
        this.hiddenFilter = hiddenFilter;
        return this;
    }

    public void setHiddenFilter(Boolean hiddenFilter) {
        this.hiddenFilter = hiddenFilter;
    }

    public Boolean isShowPartner() {
        return showPartner;
    }

    public ActivityFilter showPartner(Boolean showPartner) {
        this.showPartner = showPartner;
        return this;
    }

    public void setShowPartner(Boolean showPartner) {
        this.showPartner = showPartner;
    }

    public User getUser() {
        return user;
    }

    public ActivityFilter user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActivityFilter)) {
            return false;
        }
        return getId() != null && getId().equals(((ActivityFilter) o).getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ActivityFilter{" +
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
            ", hiddenFilter='" + isHiddenFilter() + "'" +
            ", showPartner='" + isShowPartner() + "'" +
            "}";
    }
}
