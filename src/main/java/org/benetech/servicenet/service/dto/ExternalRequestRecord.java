package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExternalRequestRecord implements Serializable {
    private String id;

    private double similarity;
}
