package org.benetech.servicenet.service.dto.external;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordRequest implements Serializable {
    private String id;

    private double similarity;
}
