package org.benetech.servicenet.adapter.smcconnect.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public abstract class ServiceRelatedSmcData extends SmcBaseData {

    @SerializedName("service_id")
    private String serviceId;
}
