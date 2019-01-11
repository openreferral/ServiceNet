package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class HealthleadsServiceAtLocation extends BaseData {

    @SerializedName("service_id")
    private String serviceId;

    @SerializedName("location_id")
    private String locationId;

    private String description;
}
