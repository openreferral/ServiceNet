package org.benetech.servicenet.adapter.icarol.model;

import lombok.Data;

@Data
public class ICarolSite extends ICarolBaseData {

    private String legalStatus;

    private String[] translations;

    private ICarolName[] names;

    private ICarolAccessibility accessibility;
}
