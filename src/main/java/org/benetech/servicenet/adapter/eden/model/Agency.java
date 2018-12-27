package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;

@Data
public class Agency  extends BaseData {

    private String description;

    private String descriptionText;

    private String coverageNote;

    private String verificationExpired;

    private String legalStatus;

    private String[] translations;

    private Name[] names;

    private CustomField[] customFields;

    private Hours[] hours;
}

