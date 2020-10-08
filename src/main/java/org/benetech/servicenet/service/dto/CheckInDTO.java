package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for the CheckIn.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID beneficiaryId;

    private String phoneNumber;

    private UUID cboId;
}
