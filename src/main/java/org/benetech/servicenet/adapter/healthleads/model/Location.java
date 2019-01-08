package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Location extends BaseData {

    @SerializedName("organization_id")
    private String organizationId;

    private String name;

    @SerializedName("alternate_name")
    private String alternateName;

    private String description;

    private String transportation;

    private String latitude;

    private String longitude;

    @SerializedName("x_schedule")
    private String schedule;

    public double getLatitude() {
        if (latitude == null || latitude.isEmpty()) {
            return 0;
        }
        return Double.valueOf(latitude);
    }

    public double getLongitude() {
        if (longitude == null || longitude.isEmpty()) {
            return 0;
        }
        return Double.valueOf(longitude);
    }
}
