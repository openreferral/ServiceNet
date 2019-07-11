package org.benetech.servicenet.adapter.healthleads.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HealthleadsPhone extends HealthleadsBaseData {

    @SerializedName("contact_id")
    private String contactId;

    private String number;

    private String extension;

    private String type;

    private String language;

    private String description;
}
