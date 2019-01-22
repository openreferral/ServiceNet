package org.benetech.servicenet.adapter.icarol.model;

import lombok.Data;

@Data
public class ICarolBaseData {

    private String id;

    private String databaseID;

    private String uniquePriorId;

    private ICarolContactDetails[] contactDetails;

    private ICarolRelated[] related;

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
