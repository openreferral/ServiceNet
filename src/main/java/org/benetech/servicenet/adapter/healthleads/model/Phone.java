package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Phone extends BaseData {

    @SerializedName("location_id")
    private String locationId;

    @SerializedName("service_id")
    private String serviceId;

    @SerializedName("organization_id")
    private String organizationId;

    @SerializedName("contact_id")
    private String contactId;

    @SerializedName("service_at_location_id")
    private String serviceAtLocationId;

    private String number;

    private String extension;

    private String type;

    private String language;

    private String description;
}
