package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EdenProgram extends EdenBaseData {

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

    private EdenName[] names;

    private EdenCustomField[] customFields;

    private EdenHours hours;

    private EdenCoverage[] coverage;
}

