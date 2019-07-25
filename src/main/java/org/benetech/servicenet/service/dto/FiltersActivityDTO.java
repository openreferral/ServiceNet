package org.benetech.servicenet.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class FiltersActivityDTO implements Serializable {

    private List<String> citiesFilterList = new ArrayList<>();

    private List<String> regionFilterList = new ArrayList<>();

    private List<String> postalCodesFilterList = new ArrayList<>();

    private List<String> taxonomiesFilterList = new ArrayList<>();

    private List<UUID> partnerFilterList = new ArrayList<>();
}

