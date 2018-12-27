package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;

@Data
public class BaseData {

    private String id;

    private String databaseID;

    private String uniquePriorId;

    private ContactDetails[] contactDetails;

    private Related[] related;

    private String cultureCode;

    private String[] searchHints;

    private String type;

    private String modified;

    private String lastVerifiedOn;

    private Boolean isFeatured;

    private Boolean isConfidential;

    private String languagesOffered;

    private String status;
}
