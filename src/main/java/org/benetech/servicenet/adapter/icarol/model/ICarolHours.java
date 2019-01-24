package org.benetech.servicenet.adapter.icarol.model;

import lombok.Data;

@Data
public class ICarolHours extends ICarolElement {

    private String note;

    private ICarolDay[] days;
}
