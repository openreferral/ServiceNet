package org.benetech.servicenet.adapter.icarol.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ICarolServiceSite extends ICarolBaseData {

    private Boolean isLinkOnly;

    private String coverageNote;

    private String verificationExpired;
}
