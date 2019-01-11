package org.benetech.servicenet.adapter.healthleads;

import org.benetech.servicenet.adapter.healthleads.model.*;

public enum DataType {
    ELIGIBILITY(Eligibility.class),
    LANGUAGES(Language.class),
    LOCATIONS(Location.class),
    ORGANIZATIONS(Organization.class),
    PHONES(Phone.class),
    PHYSICAL_ADDRESSES(PhysicalAddress.class),
    REQUIRED_DOCUMENTS(RequiredDocument.class),
    SERVICES(Service.class),
    SERVICES_AT_LOCATION(ServiceAtLocation.class),
    SERVICES_TAXONOMY(ServiceTaxonomy.class),
    TAXONOMY(Taxonomy.class);

    private Class<? extends BaseData> clazz;

    DataType(Class<? extends BaseData> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends BaseData> getClazz() {
        return clazz;
    }
}
