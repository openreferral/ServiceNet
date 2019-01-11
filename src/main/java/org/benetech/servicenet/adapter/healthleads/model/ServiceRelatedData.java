package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public abstract class ServiceRelatedData extends BaseData {

    @SerializedName("service_id")
    private String serviceId;
}
