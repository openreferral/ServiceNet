package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HealthleadsServiceAtLocation extends LocationRelatedHealthleadsData {

    @SerializedName("service_id")
    private String serviceId;

    private String description;
}
