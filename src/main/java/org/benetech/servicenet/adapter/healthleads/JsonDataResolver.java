package org.benetech.servicenet.adapter.healthleads;

import com.google.gson.Gson;
import org.benetech.servicenet.adapter.healthleads.model.*;
import org.benetech.servicenet.type.ListType;

import java.util.ArrayList;
import java.util.List;

public class JsonDataResolver {

    public List<BaseData> getDataFromJson(final String json, final String filename) {
        DataType type;
        try {
            type = DataType.valueOf(filename.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return new ArrayList<>();
        }
        switch (type) {
            case ELIGIBILITY:
                return getDataFromJson(json, Eligibility.class);
            case LANGUAGES:
                return getDataFromJson(json, Language.class);
            case LOCATIONS:
                return getDataFromJson(json, Location.class);
            case ORGANIZATIONS:
                return getDataFromJson(json, Organization.class);
            case PHONES:
                return getDataFromJson(json, Phone.class);
            case PHYSICAL_ADDRESSES:
                return getDataFromJson(json, PhysicalAddress.class);
            case REQUIRED_DOCUMENTS:
                return getDataFromJson(json, RequiredDocument.class);
            case SERVICES:
                return getDataFromJson(json, Service.class);
            case SERVICES_AT_LOCATION:
                return getDataFromJson(json, ServiceAtLocation.class);
            case SERVICES_TAXONOMY:
                return getDataFromJson(json, ServiceTaxonomy.class);
            case TAXONOMY:
                return getDataFromJson(json, Taxonomy.class);
        }
        return new ArrayList<>();
    }

    private List<BaseData> getDataFromJson(final String json, Class<? extends BaseData> clazz) {
        return new Gson().fromJson(json, new ListType<>(clazz));
    }

    private enum DataType {
        ELIGIBILITY,
        LANGUAGES,
        LOCATIONS,
        ORGANIZATIONS,
        PHONES,
        PHYSICAL_ADDRESSES,
        REQUIRED_DOCUMENTS,
        SERVICES,
        SERVICES_AT_LOCATION,
        SERVICES_TAXONOMY,
        TAXONOMY
    }
}
