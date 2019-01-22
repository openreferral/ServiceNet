package org.benetech.servicenet.adapter.icarol.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ICarolProgram extends ICarolBaseData {

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

    private ICarolName[] names;

    private ICarolCustomField[] customFields;

    private ICarolHours hours;

    private ICarolCoverage[] coverage;
}

