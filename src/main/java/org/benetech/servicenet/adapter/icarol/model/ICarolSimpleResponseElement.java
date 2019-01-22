package org.benetech.servicenet.adapter.icarol.model;

import lombok.Data;

@Data
public class ICarolSimpleResponseElement {

    private Integer id;

    private String uniquePriorID;

    private Integer databaseID;

    private String type;

    private String modified;

    private String cultureCode;
}
