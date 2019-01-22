package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EdenProgramAtSite extends EdenBaseData {

    private Boolean isLinkOnly;

    private String coverageNote;

    private String verificationExpired;
}
