package org.benetech.servicenet.adapter.icarol.model;

import lombok.Data;

@Data
public class ICarolServiceSite extends ICarolBaseData {

    private Boolean isLinkOnly;

    private String coverageNote;

    private String verificationExpired;
}
