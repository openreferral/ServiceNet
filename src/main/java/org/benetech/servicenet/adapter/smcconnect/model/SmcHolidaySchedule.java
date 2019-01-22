package org.benetech.servicenet.adapter.smcconnect.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SmcHolidaySchedule extends SmcBaseData {

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
