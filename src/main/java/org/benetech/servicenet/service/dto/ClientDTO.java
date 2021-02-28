package org.benetech.servicenet.service.dto;

import java.util.UUID;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * A DTO representing a client
 */
@Data
public class ClientDTO {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 1, max = 50)
    private String clientId;

    private String clientSecret;

    private UUID systemAccountId;

    private UUID siloId;

    @NotNull
    @Min(0)
    private Integer tokenValiditySeconds;

    public ClientDTO() {
        // Empty constructor needed for Jackson.
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Integer getTokenValiditySeconds() {
        return tokenValiditySeconds;
    }

    public void setTokenValiditySeconds(Integer tokenValiditySeconds) {
        this.tokenValiditySeconds = tokenValiditySeconds;
    }

    public UUID getSystemAccountId() {
        return systemAccountId;
    }

    public void setSystemAccountId(UUID systemAccount) {
        this.systemAccountId = systemAccount;
    }

    public UUID getSiloId() {
        return siloId;
    }

    public void setSiloId(UUID siloId) {
        this.siloId = siloId;
    }

    @Override
    public String toString() {
        return "ClientDTO: clientId: " + this.clientId + " systemAccountId: " + this.systemAccountId;
    }
}
