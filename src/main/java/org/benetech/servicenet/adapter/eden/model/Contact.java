package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;

@Data
public class Contact {

    private ContactName name;

    private String[] titles;

    private String url;

    private String purpose;

    private String number;

    private String label;

    private String description;

    private String type;
}
