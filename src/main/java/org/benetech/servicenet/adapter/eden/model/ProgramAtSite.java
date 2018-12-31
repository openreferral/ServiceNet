package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;

@Data
public class ProgramAtSite extends BaseData {

    private Boolean isLinkOnly;

    private String coverageNote;

    private String verificationExpired;
}
