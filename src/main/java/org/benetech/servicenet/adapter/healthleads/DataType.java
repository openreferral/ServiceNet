package org.benetech.servicenet.adapter.healthleads;

import org.benetech.servicenet.adapter.healthleads.model.*;

public enum DataType {
    ELIGIBILITY(HealthleadsEligibility.class),
    LANGUAGES(HealthleadsLanguage.class),
    LOCATIONS(HealthleadsLocation.class),
    ORGANIZATIONS(HealthleadsOrganization.class),
    PHONES(HealthleadsPhone.class),
    PHYSICAL_ADDRESSES(HealthleadsPhysicalAddress.class),
    REQUIRED_DOCUMENTS(HealthleadsRequiredDocument.class),
    SERVICES(HealthleadsService.class),
    SERVICES_AT_LOCATION(HealthleadsServiceAtLocation.class),
    SERVICES_TAXONOMY(HealthleadsServiceTaxonomy.class),
    TAXONOMY(HealthleadsTaxonomy.class);

    private Class<? extends BaseData> clazz;

    DataType(Class<? extends BaseData> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends BaseData> getClazz() {
        return clazz;
    }
}
