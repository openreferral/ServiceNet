package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class HealthleadsServiceAtLocation extends LocationRelatedData {

    @SerializedName("service_id")
    private String serviceId;

    private String description;
}
