package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class ServiceRelatedHealthleadsData extends HealthleadsBaseData {

    @SerializedName("service_id")
    private String serviceId;
}
