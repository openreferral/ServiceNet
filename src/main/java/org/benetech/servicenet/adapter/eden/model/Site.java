package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;

@Data
public class Site extends BaseData {

    private String legalStatus;

    private String[] translations;

    private Name[] names;

    private Accessibility accessibility;
}
