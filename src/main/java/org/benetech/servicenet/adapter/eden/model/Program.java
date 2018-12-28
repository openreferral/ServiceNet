package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;

@Data
public class Program extends BaseData {

    private String description;

    private String descriptionText;

    private String coverageNote;

    private String verificationExpired;

    private String requiredDocumentation;

    private String fees;

    private String applicationProcess;

    private String eligibility;

    private String[] taxonomy;

    private String[] translations;

    private Name[] names;

    private CustomField[] customFields;

    private Hours hours;

    private Coverage coverage;
}

