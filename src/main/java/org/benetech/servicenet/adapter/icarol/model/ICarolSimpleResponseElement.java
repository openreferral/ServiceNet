package org.benetech.servicenet.adapter.icarol.model;

import lombok.Data;
import org.benetech.servicenet.adapter.AbstractElement;

@Data
public class ICarolSimpleResponseElement extends AbstractElement {

    private String uniquePriorID;

    private Integer databaseID;

    private String type;

    private String modified;

    private String cultureCode;
}
