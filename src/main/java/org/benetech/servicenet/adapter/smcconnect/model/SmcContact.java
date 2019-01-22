package org.benetech.servicenet.adapter.smcconnect.model;

import lombok.Data;

@Data
public class SmcContact extends SmcBaseData {

    private String name;

    private String title;

    private String email;

    private String department;
}
