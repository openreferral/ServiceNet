package org.benetech.servicenet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.UUID;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import org.hibernate.annotations.GenericGenerator;

/**
 * A FieldsDisplaySettings.
 */
@Entity
@Table(name = "fields_display_settings")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FieldsDisplaySettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(name = "location_fields", joinColumns = @JoinColumn(name = "fields_display_settings_id"))
    @Column(name = "field_name")
    private List<String> locationFields;

    @ElementCollection
    @CollectionTable(name = "organization_fields", joinColumns = @JoinColumn(name = "fields_display_settings_id"))
    @Column(name = "field_name")
    private List<String> organizationFields;

    @ElementCollection
    @CollectionTable(name = "physical_address_fields", joinColumns = @JoinColumn(name = "fields_display_settings_id"))
    @Column(name = "field_name")
    private List<String> physicalAddressFields;

    @ElementCollection
    @CollectionTable(name = "postal_address_fields", joinColumns = @JoinColumn(name = "fields_display_settings_id"))
    @Column(name = "field_name")
    private List<String> postalAddressFields;

    @ElementCollection
    @CollectionTable(name = "service_fields", joinColumns = @JoinColumn(name = "fields_display_settings_id"))
    @Column(name = "field_name")
    private List<String> serviceFields;

    @ElementCollection
    @CollectionTable(
        name = "service_taxonomies_details_fields",
        joinColumns = @JoinColumn(name = "fields_display_settings_id"))
    @Column(name = "field_name")
    private List<String> serviceTaxonomiesDetailsFields;

    @ElementCollection
    @CollectionTable(name = "contact_details_fields", joinColumns = @JoinColumn(name = "fields_display_settings_id"))
    @Column(name = "field_name")
    private List<String> contactDetailsFields;

    @ManyToOne
    @JsonIgnoreProperties("fieldsDisplaySettings")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public FieldsDisplaySettings name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLocationFields() {
        return locationFields;
    }

    public FieldsDisplaySettings locationFields(List<String> locationFields) {
        this.locationFields = locationFields;
        return this;
    }

    public void setLocationFields(List<String> locationFields) {
        this.locationFields = locationFields;
    }

    public List<String> getOrganizationFields() {
        return organizationFields;
    }

    public FieldsDisplaySettings organizationFields(List<String> organizationFields) {
        this.organizationFields = organizationFields;
        return this;
    }

    public void setOrganizationFields(List<String> organizationFields) {
        this.organizationFields = organizationFields;
    }

    public List<String> getPhysicalAddressFields() {
        return physicalAddressFields;
    }

    public FieldsDisplaySettings physicalAddressFields(List<String> physicalAddressFields) {
        this.physicalAddressFields = physicalAddressFields;
        return this;
    }

    public void setPhysicalAddressFields(List<String> physicalAddressFields) {
        this.physicalAddressFields = physicalAddressFields;
    }

    public List<String> getPostalAddressFields() {
        return postalAddressFields;
    }

    public FieldsDisplaySettings postalAddressFields(List<String> postalAddressFields) {
        this.postalAddressFields = postalAddressFields;
        return this;
    }

    public void setPostalAddressFields(List<String> postalAddressFields) {
        this.postalAddressFields = postalAddressFields;
    }

    public List<String> getServiceFields() {
        return serviceFields;
    }

    public FieldsDisplaySettings serviceFields(List<String> serviceFields) {
        this.serviceFields = serviceFields;
        return this;
    }

    public void setServiceFields(List<String> serviceFields) {
        this.serviceFields = serviceFields;
    }

    public List<String> getServiceTaxonomiesDetailsFields() {
        return serviceTaxonomiesDetailsFields;
    }

    public FieldsDisplaySettings serviceTaxonomiesDetailsFields(List<String> serviceTaxonomiesDetailsFields) {
        this.serviceTaxonomiesDetailsFields = serviceTaxonomiesDetailsFields;
        return this;
    }

    public void setServiceTaxonomiesDetailsFields(List<String> serviceTaxonomiesDetailsFields) {
        this.serviceTaxonomiesDetailsFields = serviceTaxonomiesDetailsFields;
    }

    public List<String> getContactDetailsFields() {
        return contactDetailsFields;
    }

    public FieldsDisplaySettings contactDetailsFields(List<String> contactDetailsFields) {
        this.contactDetailsFields = contactDetailsFields;
        return this;
    }

    public void setContactDetailsFields(List<String> contactDetailsFields) {
        this.contactDetailsFields = contactDetailsFields;
    }

    public User getUser() {
        return user;
    }

    public FieldsDisplaySettings user(User user) {
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
        if (!(o instanceof FieldsDisplaySettings)) {
            return false;
        }
        return id != null && id.equals(((FieldsDisplaySettings) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FieldsDisplaySettings{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", locationFields='" + getLocationFields() + "'" +
            ", organizationFields='" + getOrganizationFields() + "'" +
            ", physicalAddressFields='" + getPhysicalAddressFields() + "'" +
            ", postalAddressFields='" + getPostalAddressFields() + "'" +
            ", serviceFields='" + getServiceFields() + "'" +
            ", serviceTaxonomiesDetailsFields='" + getServiceTaxonomiesDetailsFields() + "'" +
            ", contactDetailsFields='" + getContactDetailsFields() + "'" +
            "}";
    }
}
