package org.benetech.servicenet.adapter.anonymous.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class RawData {

    @SerializedName("Resource Site Phone")
    private String phone;

    @SerializedName("Line 1")
    private String address1;

    @SerializedName("Line 2")
    private String address2;

    @SerializedName("Resource Site Email")
    private String resourceSiteEmail;

    @SerializedName("Eligibility List")
    private String eligibilityList;

    @SerializedName("Is Wheel Chair Accessible")
    private String isWheelChairAccessible;

    @SerializedName("Program List")
    private String programList;

    @SerializedName("Resource Site Phone Extension")
    private String phoneExtension;

    @SerializedName("Organization Name")
    private String organizationName;

    @SerializedName("Service Description")
    private String serviceDescription;

    @SerializedName("Weekly Open Hours Raw")
    private String weeklyOpenHoursRaw;

    @SerializedName("Language List")
    private String languageList;

    @SerializedName("Service Additional Info")
    private String serviceAdditionalInfo;

    @SerializedName("City")
    private String city;

    @SerializedName("Geo Location")
    private String geoLocation;

    @SerializedName("Service Name")
    private String serviceName;

    @SerializedName("Cost of Service Minimum")
    private String costOfServiceMinimum;

    @SerializedName("State")
    private String stateProvince;

    @SerializedName("Required Item List")
    private String requiredItems;

    @SerializedName("Cost of Service Maximum")
    private String costOfServiceMaximum;

    @SerializedName("Website")
    private String website;
}
