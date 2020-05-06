package org.benetech.servicenet.service.dto.provider;

import java.io.Serializable;
import lombok.Data;

@Data
public class SimpleLocationDTO implements Serializable {

    private String address1;

    private String address2;

    private String city;

    private String ca;

    private String zipcode;
}
