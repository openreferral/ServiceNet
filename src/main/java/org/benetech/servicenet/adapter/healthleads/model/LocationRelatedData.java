package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public abstract class LocationRelatedData extends BaseData {

    @SerializedName("location_id")
    private String locationId;
}
