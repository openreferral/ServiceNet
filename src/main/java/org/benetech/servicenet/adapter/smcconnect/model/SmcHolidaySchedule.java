package org.benetech.servicenet.adapter.smcconnect.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SmcHolidaySchedule extends SmcBaseData {

    @SerializedName("location_id")
    private String locationId;

    @SerializedName("service_id")
    private String serviceId;

    @SerializedName("start_date")
    private String startDate;

    @SerializedName("end_date")
    private String endDate;

    private String closed;

    @SerializedName("opens_at")
    private String opensAt;

    @SerializedName("closes_at")
    private String closesAt;
}
