package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Data
public class ClientProfileDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private String clientId;

    private UUID systemAccount;

    @Override
    public String toString() {
        return "ClientId: " + this.clientId +
            " SystemAccount: " + this.systemAccount;
    }
}
