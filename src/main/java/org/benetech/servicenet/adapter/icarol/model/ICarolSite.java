package org.benetech.servicenet.adapter.icarol.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ICarolSite extends ICarolBaseData {

    private String legalStatus;

    private String[] translations;

    private ICarolName[] names;

    private ICarolAccessibility accessibility;
}
