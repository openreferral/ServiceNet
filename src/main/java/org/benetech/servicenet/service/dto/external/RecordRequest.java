package org.benetech.servicenet.service.dto.external;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private double similarity;
}
