package org.benetech.servicenet.adapter.icarol.model;

import lombok.Data;

@Data
public class ICarolDay extends ICarolElement {

    private String dayOfWeek;

    private String opens;

    private String closes;
}
