package org.benetech.servicenet.adapter.smcconnect.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SmcContact extends SmcBaseData {

    private String name;

    private String title;

    private String email;

    private String department;
}
