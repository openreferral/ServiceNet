package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import org.benetech.servicenet.web.rest.DateFilter;

@Data
public class FiltersActivityDTO implements Serializable {

    private List<String> citiesFilterList = new ArrayList<>();

    private List<String> regionFilterList = new ArrayList<>();

    private List<String> postalCodesFilterList = new ArrayList<>();

    private List<String> taxonomiesFilterList = new ArrayList<>();

    private List<UUID> partnerFilterList = new ArrayList<>();

    private DateFilter dateFilter;

    private LocalDate fromDate;

    private LocalDate toDate;
}
