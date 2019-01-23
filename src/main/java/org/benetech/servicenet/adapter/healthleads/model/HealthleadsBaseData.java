package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public abstract class HealthleadsBaseData {

    private String id;

    @SerializedName("location_id")
    private String locationId;

    @SerializedName("service_id")
    private String serviceId;

    @SerializedName("organization_id")
    private String organizationId;
}
