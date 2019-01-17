package org.benetech.servicenet.adapter.laac.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class LAACData {

    @SerializedName("ID")
    private String id;

    @SerializedName("Organization Name")
    private String organizationName;

    @SerializedName("Contact Name")
    private String contactName;

    @SerializedName("Phone")
    private String phone;

    @SerializedName("Authored by")
    private String authoredBy;

    @SerializedName("Authored on")
    private String authoredOn;

    @SerializedName("Address")
    private String address;

    @SerializedName("Areas Served")
    private String areasServed;

    @SerializedName("Description of Service Types")
    private String descriptionOfServiceTypes;

    @SerializedName("Eligibility")
    private String eligibility;

    @SerializedName("Intake Method")
    private String intakeMethod;

    @SerializedName("Legal Clinic Calendar URL")
    private String legalClinicCalendarURL;

    @SerializedName("Legal Help Category")
    private String legalHelpCategory;

    @SerializedName("Legal Issues")
    private String legalIssues;

    @SerializedName("Offers Legal Representation")
    private String offersLegalRepresentation;

    @SerializedName("Organization Type")
    private String organizationType;

    @SerializedName("Service Types")
    private String serviceTypes;

    @SerializedName("Spoken Languages")
    private String spokenLanguages;

    @SerializedName("Website")
    private String website;

    @SerializedName("Who We Are")
    private String whoWeAre;
}
