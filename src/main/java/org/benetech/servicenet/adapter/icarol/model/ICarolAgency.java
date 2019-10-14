package org.benetech.servicenet.adapter.icarol.model;

import java.time.ZonedDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ICarolAgency extends ICarolBaseData {

    private String description;

    private String descriptionText;

    private String coverageNote;

    private String verificationExpired;

    private String legalStatus;

    private String[] translations;

    private ICarolName[] names;

    private ICarolCustomField[] customFields;

    private ICarolHours hours;

    private ZonedDateTime lastVerifiedOn;
}

