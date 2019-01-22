package org.benetech.servicenet.adapter.smcconnect.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SmcContact extends SmcBaseData {

    @SerializedName("location_id")
    private String locationId;

    @SerializedName("organization_id")
    private String organizationId;

    @SerializedName("service_id")
    private String serviceId;

    private String name;

    private String title;

    private String email;

    private String department;
}
