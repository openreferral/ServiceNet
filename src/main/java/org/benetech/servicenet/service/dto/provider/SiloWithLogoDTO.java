package org.benetech.servicenet.service.dto.provider;

import org.benetech.servicenet.service.dto.SiloDTO;

public class SiloWithLogoDTO extends SiloDTO {

    private static final long serialVersionUID = 7L;

    private String logoBase64;

    public String getLogoBase64() {
        return logoBase64;
    }

    public void setLogoBase64(String logoBase64) {
        this.logoBase64 = logoBase64;
    }
}
