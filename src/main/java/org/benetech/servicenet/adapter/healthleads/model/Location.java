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

    private double latitude;

    private double longitude;

    @SerializedName("x_schedule")
    private String schedule;
}
