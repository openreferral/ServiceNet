package org.benetech.servicenet.adapter.sheltertech.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ScheduleRaw {

    @SerializedName("id")
    private Integer id;

    @SerializedName("schedule_days")
    private List<ScheduleDayRaw> scheduleDays;

}
