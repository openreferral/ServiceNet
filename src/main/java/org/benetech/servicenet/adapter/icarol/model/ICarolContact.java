package org.benetech.servicenet.adapter.icarol.model;

import lombok.Data;

@Data
public class ICarolContact {

    private ICarolContactName name;

    private String[] titles;

    private String url;

    private String purpose;

    private String address;

    private String number;

    private String label;

    private String description;

    private String type;

    private String line1;

    private Double latitude;

    private Double longitude;

    private String precision;

    private String county;

    private String country;

    private String stateProvince;

    private String city;

    private String zipPostalCode;
}
