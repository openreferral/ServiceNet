package org.benetech.servicenet.adapter.icarol.model;

import lombok.Data;

@Data
public class ICarolHours {

    private String note;

    private ICarolDay[] days;
}
