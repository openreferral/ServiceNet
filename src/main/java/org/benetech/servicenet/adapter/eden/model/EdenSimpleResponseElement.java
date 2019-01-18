package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;

@Data
public class EdenSimpleResponseElement {

    private Integer id;

    private String uniquePriorID;

    private Integer databaseID;

    private String type;

    private String modified;

    private String cultureCode;
}
