package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EdenAgency extends EdenBaseData {

    private String description;

    private String descriptionText;

    private String coverageNote;

    private String verificationExpired;

    private String legalStatus;

    private String[] translations;

    private EdenName[] names;

    private EdenCustomField[] customFields;

    private EdenHours hours;
}

