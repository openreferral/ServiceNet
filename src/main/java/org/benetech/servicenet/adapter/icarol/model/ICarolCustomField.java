package org.benetech.servicenet.adapter.icarol.model;

import lombok.Data;

@Data
public class ICarolCustomField extends ICarolElement {

    private String id;

    private String label;

    private Object selectedValues;
}
