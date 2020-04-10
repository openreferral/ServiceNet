package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import lombok.Data;
import org.joda.time.DateTime;

@Data
public class DataStatusDto implements Serializable {

    private String providerName;

    private DateTime lastUpdateDateTime;

    @Override
    public String toString() {
        return "ProviderName: " + this.providerName +
            " Date of last update: " + this.lastUpdateDateTime;
    }
}
