package org.benetech.servicenet.service.dto.provider;

import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Data
public class SimpleLocationDTO implements Serializable {

    private UUID id;

    private String address1;

    private String address2;

    private String city;

    private String ca;

    private String zipcode;
}
