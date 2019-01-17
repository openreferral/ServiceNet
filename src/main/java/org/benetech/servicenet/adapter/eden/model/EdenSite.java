package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;

@Data
public class EdenSite extends EdenBaseData {

    private String legalStatus;

    private String[] translations;

    private EdenName[] names;

    private EdenAccessibility accessibility;
}
