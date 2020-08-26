package org.benetech.servicenet.service.dto;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import org.benetech.servicenet.domain.enumeration.ContactDetailsFields;

/**
 * A DTO for the {@link org.benetech.servicenet.domain.ContactDetailsFieldsValue} entity.
 */
public class ContactDetailsFieldsValueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotNull
    private ContactDetailsFields contactDetailsField;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ContactDetailsFields getContactDetailsField() {
        return contactDetailsField;
    }

    public void setContactDetailsField(ContactDetailsFields contactDetailsField) {
        this.contactDetailsField = contactDetailsField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContactDetailsFieldsValueDTO contactDetailsFieldsValueDTO = (ContactDetailsFieldsValueDTO) o;
        if (contactDetailsFieldsValueDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), contactDetailsFieldsValueDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ContactDetailsFieldsValueDTO{" +
            "id=" + getId() +
            ", contactDetailsField='" + getContactDetailsField() + "'" +
            "}";
    }
}
