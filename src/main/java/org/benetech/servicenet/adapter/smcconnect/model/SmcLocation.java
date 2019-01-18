package org.benetech.servicenet.adapter.smcconnect.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class SmcLocation extends SmcBaseData {

    private String accessibility;

    @SerializedName("alternate_name")
    private String alternateName;

    @SerializedName("admin_emails")
    private String adminEmails;

    private String description;

    private String email;

    private String hours;

    private String kind;

    private String languages;

    private String latitude;

    private String longitude;

    @SerializedName("market_match")
    private String marketMatch;

    private String name;

    private String payments;

    private String products;

    @SerializedName("short_desc")
    private String shortDesc;

    private String transportation;

    private String website;

    private String virtual;
}
