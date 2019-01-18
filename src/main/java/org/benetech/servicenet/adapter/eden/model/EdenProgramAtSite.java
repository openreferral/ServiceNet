package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;

@Data
public class EdenProgramAtSite extends EdenBaseData {

    private Boolean isLinkOnly;

    private String coverageNote;

    private String verificationExpired;
}
