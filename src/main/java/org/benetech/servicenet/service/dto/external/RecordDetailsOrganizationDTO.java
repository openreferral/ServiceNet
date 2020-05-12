package org.benetech.servicenet.service.dto.external;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.benetech.servicenet.service.dto.ConflictDTO;
import org.benetech.servicenet.service.dto.ContactDTO;
import org.benetech.servicenet.service.dto.LocationRecordDTO;
import org.benetech.servicenet.service.dto.PhoneDTO;
import org.benetech.servicenet.service.dto.ProgramDTO;
import org.benetech.servicenet.service.dto.ServiceRecordDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordDetailsOrganizationDTO implements Serializable {

    private UUID id;

    @NotNull
    private String name;

    private String alternateName;

    @Lob
    private String description;

    @Size(max = 50)
    private String email;

    private String url;

    private String taxStatus;

    private String taxId;

    private LocalDate yearIncorporated;

    private String legalStatus;

    @NotNull
    private Boolean active;

    private ZonedDateTime updatedAt;

    private ZonedDateTime lastVerifiedOn;

    private UUID replacedById;

    private UUID sourceDocumentId;

    private String sourceDocumentDateUploaded;

    private UUID accountId;

    private String accountName;

    private String externalDbId;

    private ZonedDateTime lastUpdated;

    private Set<LocationRecordDTO> locations;

    private Set<ServiceRecordDTO> services;

    private Set<ContactDTO> contacts;

    private Set<ProgramDTO> programs;

    private Set<PhoneDTO> phones;

    private List<ConflictDTO> conflicts;

}
