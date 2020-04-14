package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class DataStatusDto implements Serializable {

    private String providerName;

    private LocalDateTime lastUpdateDateTime;

    @Override
    public String toString() {
        return "ProviderName: " + this.providerName +
            " Date of last update: " + this.lastUpdateDateTime;
    }
}
