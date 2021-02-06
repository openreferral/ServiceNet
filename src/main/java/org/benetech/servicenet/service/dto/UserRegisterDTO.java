package org.benetech.servicenet.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A DTO representing a user, with his authorities.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRegisterDTO extends UserDTO {

    private String password;

    private String contactFirstName;

    private String contactLastName;

    private String contactEmail;
}
