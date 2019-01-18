package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;

@Data
public class EdenBaseData {

    private String id;

    private String databaseID;

    private String uniquePriorId;

    private EdenContactDetails[] contactDetails;

    private EdenRelated[] related;

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
