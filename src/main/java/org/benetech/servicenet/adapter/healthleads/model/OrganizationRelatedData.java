package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public abstract class OrganizationRelatedData extends BaseData {

    @SerializedName("organization_id")
    private String organizationId;
}
