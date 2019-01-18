package org.benetech.servicenet.adapter.smcconnect.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public abstract class LocationRelatedSmcData extends SmcBaseData {

    @SerializedName("location_id")
    private String locationId;
}
