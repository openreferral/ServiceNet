package org.benetech.servicenet.service.dto;

import lombok.Data;

@Data
public class OpeningHoursRow {

    private static final long serialVersionUID = 1L;

    private Integer[] activeDays;

    private String from;

    private String to;

    public OpeningHoursRow() {
        // Empty constructor needed for Jackson.
    }

}
