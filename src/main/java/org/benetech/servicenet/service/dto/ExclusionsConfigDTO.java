package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.ExclusionsConfig} entity.
 */
public class ExclusionsConfigDTO implements Serializable {

    private UUID id;

    private UUID accountId;

    private String accountName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID systemAccountId) {
        this.accountId = systemAccountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String systemAccountName) {
        this.accountName = systemAccountName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExclusionsConfigDTO exclusionsConfigDTO = (ExclusionsConfigDTO) o;
        if (exclusionsConfigDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), exclusionsConfigDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExclusionsConfigDTO{" +
            "id=" + getId() +
            ", account=" + getAccountId() +
            ", account='" + getAccountName() + "'" +
            "}";
    }
}
