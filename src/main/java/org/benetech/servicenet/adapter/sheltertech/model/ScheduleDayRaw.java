package org.benetech.servicenet.adapter.sheltertech.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class ScheduleDayRaw {

    @SerializedName("id")
    private Integer id;

    @SerializedName("day")
    private String scheduleDays;

    @SerializedName("opens_at")
    private Integer opensAt;

    @SerializedName("closes_at")
    private Integer closesAt;

    @SerializedName("open_time")
    private Integer openTime;

    @SerializedName("close_time")
    private Integer closeTime;
}
