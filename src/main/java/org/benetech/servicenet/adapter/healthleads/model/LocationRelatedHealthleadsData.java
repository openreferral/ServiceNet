package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class LocationRelatedHealthleadsData extends HealthleadsBaseData {

    @SerializedName("location_id")
    private String locationId;
}
