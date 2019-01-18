package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;

@Data
public class EdenHours {

    private String note;

    private EdenDay[] days;
}
