package org.benetech.servicenet.adapter.smcconnect.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SmcRegularSchedule extends SmcBaseData {

    @SerializedName("location_id")
    private String locationId;

    @SerializedName("service_id")
    private String serviceId;

    private String weekday;

    @SerializedName("opens_at")
    private String opensAt;

    @SerializedName("closes_at")
    private String closesAt;
}
