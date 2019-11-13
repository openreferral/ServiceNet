package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import java.time.ZonedDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HealthleadsServiceMetadata extends HealthleadsBaseData {

    @SerializedName("resource_id")
    private String serviceId;

    @SerializedName("last_action_date")
    private ZonedDateTime lastActionDate;

    @SerializedName("updated_by")
    private String updatedBy;

    @SerializedName("last_action_type")
    private String lastActionType;
}
