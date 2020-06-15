package org.benetech.servicenet.service.dto;

import lombok.Data;

/**
 * A DTO representing a user, with his authorities.
 */
@Data
public class UserRegisterDTO extends UserDTO {

    private String password;

}
