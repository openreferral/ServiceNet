package org.benetech.servicenet.service.dto.provider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ProviderFilterDTO implements Serializable {

    private String city;

    private String region;

    private String zip;

    private List<String> serviceTypes = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder serviceTypesString = new StringBuilder();
        for (String s : serviceTypes) {
            serviceTypesString.append(s);
            serviceTypesString.append(" | ");
        }

        return "ProviderFilterDTO" + " city: " + city + " region: " + region + " zip: " + zip + " "
            + " serviceTypes " + serviceTypesString.toString();
    }
}
