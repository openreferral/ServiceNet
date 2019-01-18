package org.benetech.servicenet.adapter.sheltertech.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrganizationRaw {

    @SerializedName("alternate_name")
    private String alternateName;

    @SerializedName("certified")
    private Boolean certified;

    @SerializedName("email")
    private String email;

    @SerializedName("id")
    private String externalDbId;

    @SerializedName("legal_status")
    private String legalStatus;

    @SerializedName("long_description")
    private String longDescription;

    @SerializedName("name")
    private String name;

    @SerializedName("short_description")
    private String shortDescription;

    @SerializedName("status")
    private String status;

    @SerializedName("verified_at")
    private LocalDateTime verifiedAt;

    @SerializedName("website")
    private String website;

    @SerializedName("certified_at")
    private LocalDateTime certifiedAt;

    @SerializedName("services")
    private String services;

    @SerializedName("schedule")
    private String schedule;

    @SerializedName("phones")
    private String phones;

    @SerializedName("address")
    private String address;

    @SerializedName("notes")
    private String notes;

    @SerializedName("categories")
    private String categories;

    @SerializedName("ratings")
    private String ratings;

}
