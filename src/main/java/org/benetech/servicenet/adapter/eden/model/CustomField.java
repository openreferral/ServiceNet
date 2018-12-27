package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;

@Data
public class CustomField {

    private String id;

    private String label;

    private Object[] selectedValues;
}
