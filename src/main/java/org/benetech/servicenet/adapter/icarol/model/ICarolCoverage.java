package org.benetech.servicenet.adapter.icarol.model;

import lombok.Data;

@Data
public class ICarolCoverage extends ICarolElement {

    private String purpose;

    private String county;

    private String stateProvince;

    private String country;

    private String type;
}
